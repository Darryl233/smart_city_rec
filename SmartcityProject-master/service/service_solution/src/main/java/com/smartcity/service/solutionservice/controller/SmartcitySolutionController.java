package com.smartcity.service.solutionservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartcity.common.commonutils.BlockChainController;
import com.smartcity.common.commonutils.JwtUtils;
import com.smartcity.common.commonutils.R;
import com.smartcity.common.commonutils.entity.BlockChainResource;
import com.smartcity.common.commonutils.entity.CommonHistory;
import com.smartcity.service.solutionservice.client.UserHistoryClient;
import com.smartcity.service.solutionservice.entity.SmartcitySolution;
import com.smartcity.service.solutionservice.entity.vo.SolutionQuery;
import com.smartcity.service.solutionservice.service.SmartcitySolutionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 解决方案 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-26
 */
@Api(description = "解决方案管理")
@RestController
@RequestMapping("/solutionservice/solution")
public class SmartcitySolutionController {
    @Autowired
    private SmartcitySolutionService solutionService;

    @Autowired
    private UserHistoryClient historyClient;

    @PostMapping(value = "/import")
    public R addSolutionByExcel(MultipartFile file){
        solutionService.saveByExcel(file, solutionService);
        return R.ok();
    }

    @ApiOperation(value = "查询所有方案")
    @GetMapping("/findAll")
    public R findAll(){
        List<SmartcitySolution> list = solutionService.list(null);
        return R.ok().data("items", list);
    }

    @ApiOperation(value = "根据id，逻辑删除解决方案")
    @DeleteMapping(value = "/remove/{id}")
    public R removeById(
            @ApiParam(name = "id", value = "解决方案id", required = true)
            @PathVariable String id
    ){
        boolean remove = solutionService.removeById(id);
        if (remove)
            return R.ok();
        else
            return R.error().message("请稍后重试");
    }

    @ApiOperation(value = "条件分页方案列表")
    @PostMapping(value = "/pageSolutionCondition/{page}/{limit}")
    public R pageSolutionCondition(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "condition", value = "条件", required = false)
            @RequestBody(required = false) SolutionQuery solutionQuery
    ){
        Page<SmartcitySolution> pageParam = new Page<>(page, limit);
        QueryWrapper<SmartcitySolution> wrapper = new QueryWrapper<>();
        String title = solutionQuery.getTitle();
        String requirementNumber = solutionQuery.getRequirementNumber();
        String orgName = solutionQuery.getOrgName();
        String keywords = solutionQuery.getKeywords();
        String price_left = solutionQuery.getPrice_left();
        String price_right = solutionQuery.getPrice_right();

        if (!StringUtils.isEmpty(title))
            wrapper.like("title", title);
        if (!StringUtils.isEmpty(requirementNumber))
            wrapper.like("requirement_number", requirementNumber);
        if (!StringUtils.isEmpty(orgName))
            wrapper.like("org_name", orgName);
        if (!StringUtils.isEmpty(keywords))
            wrapper.like("keywords", keywords);
        if(!StringUtils.isEmpty(price_left))
            wrapper.ge("price", price_left);
        if(!StringUtils.isEmpty(price_right))
            wrapper.le("price", price_right);

        wrapper.orderByDesc("id");

        solutionService.page(pageParam, wrapper);

        List<SmartcitySolution> solutionList = pageParam.getRecords();
        long total = pageParam.getTotal();

        return R.ok().data("total", total).data("rows", solutionList);
    }

    @ApiOperation(value = "添加解决方案")
    @PostMapping(value = "/addSolution")
    public R addSolution(
            @ApiParam(name = "solution", value = "解决方案", required = true)
            @RequestBody(required = true) SmartcitySolution solution,
            HttpServletRequest request
    ){
        boolean save = solutionService.save(solution);
        if (save) {
            if (request.getHeader("admin")!=null){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                BlockChainResource resource = new BlockChainResource("0", "20", "null",
                        "Solution_"+solution.getId(), formatter.format(solution.getGmtCreate()), "Admin_1");
                BlockChainController.setBlockChainResource(resource);
            }
            return R.ok().data("solution", solution);
        }
        else
            return R.error().message("所插入数据有误，未插入数据库中，请稍后重试");
    }

    @ApiOperation(value = "根据Id查询解决方案")
    @GetMapping(value = "/getSolution/{id}")
    public R getSolutionById(
            @ApiParam(name = "id", value = "解决方案id", required = true)
            @PathVariable String id,
            HttpServletRequest request
    ){
        SmartcitySolution solution = solutionService.getById(id);

        if (request.getHeader("admin") != null){
            return R.ok().data("solution", solution);
        }

        //调用jwt工具类，根据request获取头信息，返回用户id,不是user_id
        String logId = JwtUtils.getMemberIdByJwtToken(request);
        CommonHistory history = new CommonHistory();
        history.setLogId(logId);
        history.setResourceType(4);
        history.setKgId(solution.getKgId());
        boolean saveHistory = historyClient.saveHistory(history);
        if (saveHistory)
            return R.ok().data("solution", solution);
        else
            return R.ok().message("添加浏览记录失败").data("solution", solution);
    }

    @ApiOperation(value = "修改项目需求")
    @PostMapping(value = "/updateSolution")
    public R updateSolution(
            @ApiParam(name = "solution", value = "解决方案", required = true)
            @RequestBody(required = true) SmartcitySolution solution
    ){
        boolean update = solutionService.updateById(solution);
        if (update)
            return R.ok().data("solution", solution);
        else
            return R.error().message("所更新数据存在错误，更新数据库失败");
    }

    @ApiOperation(value = "根据IDList获取解决方案batch，自动补全到10个")
    @GetMapping(value = "/getSolutionBatch")
    public R getBatch(@RequestParam("IDList") List<String> IDList){
        List<SmartcitySolution> solutionList = new ArrayList<>();
        QueryWrapper<SmartcitySolution> wrapper = new QueryWrapper<>();
        for (int i=0; i<IDList.size(); i++){
            wrapper.ne("kg_id", IDList.get(i));
            QueryWrapper<SmartcitySolution> tmp = new QueryWrapper<>();
            tmp.eq("kg_id", IDList.get(i));
            SmartcitySolution solution = solutionService.getOne(tmp);
            solutionList.add(solution);
        }
        if (IDList.size()<10){
            wrapper.last("limit " + String.valueOf(10-IDList.size()));
            solutionList.addAll(solutionService.list(wrapper));
        }
        return R.ok().data("items", solutionList);
    }

    @ApiOperation(value = "根据RequirementNumber获取解决方案List")
    @GetMapping(value = "/getSolutionListByRequirementNumber/{requirementNumber}")
    public R getSolutionListByRequirementNumber(
            @ApiParam(name = "requirementNumber", value = "项目需求编号", required = true)
            @PathVariable String requirementNumber
    ){
        QueryWrapper<SmartcitySolution> wrapper = new QueryWrapper<>();
        wrapper.eq("requirement_number", requirementNumber);
        List<SmartcitySolution> solutionList = solutionService.list(wrapper);
        return R.ok().data("items", solutionList);
    }

    @ApiOperation(value = "根据id修改方案的接受状态")
    @GetMapping(value = "/modifyIsAccepted/{id}/{isAccepted}")
    public R modifyIsAccepted(
            @ApiParam(name = "id", value = "解决方案id", required = true)
            @PathVariable String id,
            @ApiParam(name = "isAccepted", value = "是否接受", required = true)
            @PathVariable boolean isAccepted
    ){
        SmartcitySolution solution = new SmartcitySolution();
        solution.setId(id);
        solution.setIsAccepted(isAccepted);
        boolean update = solutionService.updateById(solution);
        if (update){
            return R.ok().message("该方案接受状态修改成功");
        }else{
            return R.error().message("修改失败，请稍后重试");
        }
    }
}

