package com.smartcity.service.requirementservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.common.commonutils.BlockChainController;
import com.smartcity.common.commonutils.JwtUtils;
import com.smartcity.common.commonutils.R;
import com.smartcity.common.commonutils.entity.BlockChainResource;
import com.smartcity.common.commonutils.entity.CommonHistory;
import com.smartcity.service.requirementservice.client.UserHistoryClient;
import com.smartcity.service.requirementservice.entity.SmartcityRequirement;
import com.smartcity.service.requirementservice.entity.vo.RequirementQuery;
import com.smartcity.service.requirementservice.service.SmartcityRequirementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 项目需求 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-26
 */
@Api(description = "项目需求管理")
@RestController
@RequestMapping("/requirementservice/requirement")
public class SmartcityRequirementController {
    @Autowired
    private SmartcityRequirementService requirementService;

    @Autowired
    private UserHistoryClient historyClient;

    @PostMapping("/import")
    public R addRequirementByExcel(MultipartFile file){
        requirementService.saveByExcel(file, requirementService);
        return R.ok();
    }

    @ApiOperation(value = "查询所有需求")
    @GetMapping("/findAll")
    public R findAll(){
        List<SmartcityRequirement> list = requirementService.list(null);
        return R.ok().data("items", list);
    }

    @ApiOperation(value = "根据id，逻辑删除项目需求")
    @DeleteMapping(value = "/remove/{id}")
    public R removeById(
            @ApiParam(name = "id", value = "需求id", required = true)
            @PathVariable String id
    ){
        boolean remove = requirementService.removeById(id);
        if (remove){
            return R.ok();
        }else {
            return R.error();
        }
    }

    @ApiOperation(value = "条件分页需求列表")
    @PostMapping(value = "/pageRequirementCondition/{page}/{limit}")
    public R pageRequirementCondition(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "condition", value = "条件", required = false)
            @RequestBody(required = false) RequirementQuery requirementQuery
            ){
        Page<SmartcityRequirement> pageParam = new Page<>(page, limit);
        QueryWrapper<SmartcityRequirement> wrapper = new QueryWrapper<>();
        String title = requirementQuery.getTitle();
        String purchaseInstitution = requirementQuery.getPurchaseInstitution();
        String contentDescription = requirementQuery.getContentDescription();
        String announceTime_begin = requirementQuery.getAnnounceTime_begin();
        String announceTime_end = requirementQuery.getAnnounceTime_end();
        String openTime_begin = requirementQuery.getOpenTime_begin();
        String openTime_end = requirementQuery.getOpenTime_end();
        String price_left = requirementQuery.getPrice_left();
        String price_right = requirementQuery.getPrice_right();

        if (!StringUtils.isEmpty(title))
            wrapper.like("title", title);
        if (!StringUtils.isEmpty(purchaseInstitution))
            wrapper.like("purchase_institution", purchaseInstitution);
        if (!StringUtils.isEmpty(contentDescription))
            wrapper.like("content_description", contentDescription);
        if (!StringUtils.isEmpty(announceTime_begin))
            wrapper.ge("announce_time", announceTime_begin);
        if (!StringUtils.isEmpty(announceTime_end))
            wrapper.le("announce_time", announceTime_end);
        if (!StringUtils.isEmpty(openTime_begin))
            wrapper.ge("open_time", openTime_begin);
        if (!StringUtils.isEmpty(openTime_end))
            wrapper.le("open_time", openTime_end);
        if(!StringUtils.isEmpty(price_left)){
            wrapper.ge("price", price_left);
        }
        if(!StringUtils.isEmpty(price_right)){
            wrapper.le("price", price_right);
        }

        wrapper.orderByDesc("id");

        requirementService.page(pageParam, wrapper);

        List<SmartcityRequirement> requirementList = pageParam.getRecords();
        long total = pageParam.getTotal();

        return R.ok().data("total", total).data("rows", requirementList);
    }

    @ApiOperation(value = "添加项目需求")
    @PostMapping(value = "/addRequirement")
    public R addRequirement(
            @ApiParam(name = "requirement", value = "项目需求", required = true)
            @RequestBody(required = true) SmartcityRequirement requirement,
            HttpServletRequest request
    ){
        boolean save = requirementService.save(requirement);
        if (save) {
            if (request.getHeader("admin") != null){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                BlockChainResource resource = new BlockChainResource("0", "20", "null",
                        "Requirement_"+requirement.getId(), formatter.format(requirement.getGmtCreate()), "Admin_1");
                BlockChainController.setBlockChainResource(resource);
            }
            return R.ok().data("requirement", requirement);
        }
        else {
            return R.error().message("所插入数据有误，未插入数据库中");
        }
    }



    @ApiOperation(value = "根据ID查询项目需求")
    @GetMapping(value = "/getRequirement/{id}")
    public R getRequirementById(
            @ApiParam(name = "id", value = "项目需求ID", required = true)
            @PathVariable String id,
            HttpServletRequest request
    ){
        SmartcityRequirement requirement = requirementService.getById(id);

        if (request.getHeader("admin") != null){
            return R.ok().data("requirement", requirement);
        }

        //调用jwt工具类，根据request获取头信息，返回用户id,不是user_id
        String logId = JwtUtils.getMemberIdByJwtToken(request);
        CommonHistory history = new CommonHistory();
        history.setLogId(logId);
        history.setResourceType(3);
        history.setKgId(requirement.getKgId());
        boolean saveHistory = historyClient.saveHistory(history);
        if (saveHistory)
            return R.ok().data("requirement", requirement);
        else
            return R.ok().message("添加浏览记录失败").data("requirement", requirement);
    }

    @ApiOperation(value = "修改项目需求")
    @PostMapping(value = "/updateRequirement")
    public R updateRequirement(
            @ApiParam(name = "requirement", value = "项目需求", required = true)
            @RequestBody(required = true) SmartcityRequirement requirement
    ){
        boolean update = requirementService.updateById(requirement);
        if (update)
            return R.ok().data("requirement", requirement);
        else
            return R.error().message("所更新数据存在错误，更新数据库失败");
    }

    @ApiOperation(value = "根据IDList获取项目需求batch, 自动补全到10个")
    @GetMapping(value = "/getRequirementBatch")
    public R getBatch(@RequestParam("IDList") List<String> IDList){
        List<SmartcityRequirement> requirementList = new ArrayList<>();
        QueryWrapper<SmartcityRequirement> wrapper = new QueryWrapper<>();
        for (int i=0; i<IDList.size(); i++){
            wrapper.ne("kg_id", IDList.get(i));
            QueryWrapper<SmartcityRequirement> tmp = new QueryWrapper<>();
            tmp.eq("kg_id", IDList.get(i));
            SmartcityRequirement requirement = requirementService.getOne(tmp);
            requirementList.add(requirement);
        }
        if (IDList.size()<10){
            wrapper.last("limit " + String.valueOf(10-IDList.size()));
            requirementList.addAll(requirementService.list(wrapper));
        }
        return R.ok().data("items", requirementList);
    }
}

