package com.smartcity.service.caseservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartcity.common.commonutils.BlockChainController;
import com.smartcity.common.commonutils.JwtUtils;
import com.smartcity.common.commonutils.R;
import com.smartcity.common.commonutils.entity.BlockChainResource;
import com.smartcity.common.commonutils.entity.CommonHistory;
import com.smartcity.service.caseservice.client.UserHistoryClient;
import com.smartcity.service.caseservice.entity.SmartcityCase;
import com.smartcity.service.caseservice.entity.vo.CaseQuery;
import com.smartcity.service.caseservice.service.SmartcityCaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 案例 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-26
 */
@Api(description = "案例管理")
@RestController
@RequestMapping("/caseservice/case")
public class SmartcityCaseController {
    @Autowired
    private SmartcityCaseService caseService;

    @Autowired
    private UserHistoryClient historyClient;

    @PostMapping(value = "/import")
    public R addCaseByExcel(MultipartFile file){
        caseService.saveByExcel(file, caseService);
        return R.ok();
    }

    @ApiOperation(value = "查询所有案例")
    @GetMapping("/findAll")
    public R findAll(){
        List<SmartcityCase> list = caseService.list(null);
        return R.ok().data("items", list);
    }

    @ApiOperation(value = "根据id，逻辑删除")
    @DeleteMapping(value = "/remove/{id}")
    public R removeById(
            @ApiParam(name = "id", value = "案例id", required = true)
            @PathVariable String id
    ){
        boolean remove = caseService.removeById(id);
        if (remove){
            return R.ok();
        }else {
            return R.error();
        }
    }

    @ApiOperation(value = "条件分页需求列表")
    @PostMapping(value = "/pageCaseCondition/{page}/{limit}")
    public R pageCaseCondition(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "condition", value = "条件", required = false)
            @RequestBody(required = false) CaseQuery caseQuery
    ){
        Page<SmartcityCase> pageParam = new Page<>(page, limit);
        QueryWrapper<SmartcityCase> wrapper = new QueryWrapper<>();

        String title = caseQuery.getTitle();
        String staffInstitution = caseQuery.getStaffInstitution();
        String introduction = caseQuery.getIntroduction();
        String indicator = caseQuery.getIndicator();
        String origin = caseQuery.getOrigin();
        String application = caseQuery.getApplication();
        String price_left = caseQuery.getPrice_left();
        String price_right = caseQuery.getPrice_right();
        if (!StringUtils.isEmpty(title))
            wrapper.like("title", title);
        if (!StringUtils.isEmpty(staffInstitution))
            wrapper.like("staff_institution", staffInstitution);
        if (!StringUtils.isEmpty(introduction))
            wrapper.like("introduction", introduction);
        if (!StringUtils.isEmpty(indicator))
            wrapper.like("indicator", indicator);
        if (!StringUtils.isEmpty(origin))
            wrapper.like("origin", origin);
        if (!StringUtils.isEmpty(application))
            wrapper.like("application", application);
        if(!StringUtils.isEmpty(price_left))
            wrapper.ge("price", price_left);
        if(!StringUtils.isEmpty(price_right))
            wrapper.le("price", price_right);

        wrapper.orderByDesc("id");

        caseService.page(pageParam, wrapper);

        List<SmartcityCase> caseList = pageParam.getRecords();
        long total = pageParam.getTotal();

        return R.ok().data("total", total).data("rows", caseList);
    }

    @ApiOperation(value = "添加案例")
    @PostMapping(value = "/addCase")
    public R addCase(
            @ApiParam(name = "case", value = "案例", required = true)
            @RequestBody(required = true) SmartcityCase smartcityCase,
            HttpServletRequest request
    ){
        boolean save = caseService.save(smartcityCase);
        if (save) {
            if (request.getHeader("admin")!=null){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                BlockChainResource resource = new BlockChainResource("0", "20", "null",
                        "Case_"+smartcityCase.getId(), formatter.format(smartcityCase.getGmtCreate()), "Admin_1");
                BlockChainController.setBlockChainResource(resource);
            }
            return R.ok().data("case", smartcityCase);
        }
        else
            return R.error().message("所插入数据有误，未插入数据库中");
    }

    @ApiOperation(value = "根据ID查询案例")
    @GetMapping(value = "/getCase/{id}")
    public R getCaseById(
            @ApiParam(name = "id", value = "案例ID", required = true)
            @PathVariable String id,
            HttpServletRequest request
    ){
        SmartcityCase smartcityCase = caseService.getById(id);

        if (request.getHeader("admin") != null){
            return R.ok().data("case", smartcityCase);
        }

        //调用jwt工具类，根据request获取头信息，返回用户id,不是user_id
        String logId = JwtUtils.getMemberIdByJwtToken(request);
        CommonHistory history = new CommonHistory();
        history.setLogId(logId);
        history.setResourceType(5);
        history.setKgId(smartcityCase.getKgId());
        boolean saveHistory = historyClient.saveHistory(history);
        if (saveHistory)
            return R.ok().data("case", smartcityCase);
        else
            return R.ok().message("添加浏览记录失败").data("case", smartcityCase);
    }

    @ApiOperation(value = "修改案例信息")
    @PostMapping(value = "/updateCase")
    public R updateCase(
            @ApiParam(name = "case", value = "案例", required = true)
            @RequestBody(required = true) SmartcityCase smartcityCase
    ){
        boolean update = caseService.updateById(smartcityCase);
        if (update)
            return R.ok().data("case", smartcityCase);
        else
            return R.error().message("所更新数据存在错误，更新数据库失败");
    }

    @ApiOperation(value = "根据IDList获取案例batch")
    @GetMapping(value = "/getCaseBatch")
    public R getBatch(@RequestParam("IDList") List<String> IDList){
        List<SmartcityCase> caseList = new ArrayList<>();
        QueryWrapper<SmartcityCase> wrapper = new QueryWrapper<>();
        for (int i=0; i<IDList.size(); i++){
            wrapper.ne("kg_id", IDList.get(i));
            QueryWrapper<SmartcityCase> tmp = new QueryWrapper<>();
            tmp.eq("kg_id", IDList.get(i));
            SmartcityCase smartcityCase = caseService.getOne(tmp);
            caseList.add(smartcityCase);
        }
        if (caseList.size()<10){
            wrapper.last("limit "+String.valueOf(10-IDList.size()));
            caseList.addAll(caseService.list(wrapper));
        }
        return R.ok().data("items", caseList);
    }
}

