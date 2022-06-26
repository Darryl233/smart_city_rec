package com.smartcity.resources_crud.controller.institution;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartcity.common.commonutils.JwtUtils;
import com.smartcity.common.commonutils.R;
import com.smartcity.common.commonutils.entity.CommonHistory;
import com.smartcity.common.commonutils.entity.Institution;
import com.smartcity.resources_crud.client.UserHistoryClient;
import com.smartcity.resources_crud.entity.institution.SmartcityInstitution;
import com.smartcity.resources_crud.entity.institution.vo.InstitutionQuery;
import com.smartcity.resources_crud.service.institution.impl.SmartcityInstitutionServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 单位 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-20
 */
@RestController
@RequestMapping("/institutionservice/institution")
public class SmartcityInstitutionController {

    @Autowired
    private SmartcityInstitutionServiceImpl institutionService;

    @Autowired
    private UserHistoryClient historyClient;

    //通过上传文件excel添加单位信息
    @ApiOperation(value = "通过上传文件excel添加单位信息")
    @PostMapping("/import")
    public R addPatentByExcel(MultipartFile file){
        institutionService.saveByExcel(file, institutionService);
        return R.ok();
    }

    @ApiOperation(value = "查询所有单位")
    @GetMapping("/findAll")
    public R findAllInstitution(){
        List<SmartcityInstitution> institutionList = institutionService.list(null);
        return R.ok().data("items", institutionList);
    }

    @ApiOperation(value = "根据id，逻辑删除单位")
    @DeleteMapping("/remove/{id}")
    public R removeById(
            @ApiParam(name = "id", value = "单位id", required = true)
            @PathVariable String id
    ){
        boolean remove = institutionService.removeById(id);
        if (remove){
            return R.ok();
        }else{
            return R.error().message("删除失败，请稍后重试");
        }
    }

    @ApiOperation(value = "条件分页单位列表")
    @PostMapping(value = "/pageInstitutionCondition/{page}/{limit}")
    public R pageInstitutionCondition(
            @ApiParam(name = "page", value = "当前页", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "condition", value = "条件", required = false)
            @RequestBody(required = false) InstitutionQuery institutionQuery
            ){
        //创建page对象
        Page<SmartcityInstitution> pageParam = new Page<>(page, limit);
        //构建条件wrapper
        QueryWrapper<SmartcityInstitution> wrapper = new QueryWrapper<>();
        //多条件组合查询
        //判断条件值是否为空，如果不为空则拼接条件
        String name = institutionQuery.getName();
        String classification = institutionQuery.getClassification();
        String industry = institutionQuery.getIndustry();
        String begin = institutionQuery.getBegin();
        String end = institutionQuery.getEnd();
        if (!StringUtils.isEmpty(name)){
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(industry)){
            wrapper.like("industry", industry);
        }
        if (!StringUtils.isEmpty(classification)){
            wrapper.like("classification", classification);
        }
        if(!StringUtils.isEmpty(begin)){
            wrapper.ge("createtime", begin);
        }
        if(!StringUtils.isEmpty(end)){
            wrapper.le("createtime", end);
        }

        //排序
        wrapper.orderByDesc("gmt_create");
        //调用方法实现条件查询
        institutionService.page(pageParam, wrapper);
        List<SmartcityInstitution> records = pageParam.getRecords();
        long total = pageParam.getTotal();

        return  R.ok().data("total", total).data("rows", records);
    }

    //使用@RequestBody，接收前端json数据，需要用post
    @ApiOperation(value = "添加单位")
    @PostMapping("/addInstitution")
    public R addInstitution(
            @ApiParam(name = "institution", value = "单位", required = true)
            @RequestBody(required = true) SmartcityInstitution institution
    ){
        boolean save = institutionService.save(institution);
        if(save){
            return R.ok().data("institution", institution);
        }
        else{
            return R.error().message("添加失败，请稍后重试");
        }
    }

    @ApiOperation(value = "根据ID查询单位")
    @GetMapping("/getInstitution/{id}")
    public R getInstitutionById(
            @ApiParam(name = "id", value = "单位id", required = true)
            @PathVariable String id,
            HttpServletRequest request
    ){
        SmartcityInstitution institution = institutionService.getById(id);

        if (request.getHeader("admin") != null){
            return R.ok().data("institution", institution);
        }

        //调用jwt工具类，根据request获取头信息，返回用户id,不是user_id
        String logId = JwtUtils.getMemberIdByJwtToken(request);
        CommonHistory history = new CommonHistory();
        history.setLogId(logId);
        history.setResourceType(2);
        history.setKgId(institution.getKgId());
        boolean saveHistory = historyClient.saveHistory(history);
        if (saveHistory)
            return R.ok().data("institution", institution);
        else
            return R.ok().message("添加浏览记录失败").data("institution", institution);
    }

    @ApiOperation(value = "修改单位信息")
    @PostMapping(value = "/updateInstitution")
    public R updateInstitution(
            @ApiParam(name = "institution", value = "单位信息", required = true)
            @RequestBody(required = true) SmartcityInstitution institution
    ){
        boolean update = institutionService.updateById(institution);
        if(update){
            return R.ok().data("institution", institution);
        }
        else{
            return R.error().message("更新单位信息失败，请稍后重试");
        }
    }

    //根据ID获取单位batch
    @ApiOperation(value = "根据ID获取单位batch")
    @GetMapping("/getInstitutionBatch")
    public R getBatch(@RequestParam("IDList") List<String> IDList){
        //System.out.println(IDList);
        List<SmartcityInstitution> institutionList = new ArrayList<>();
        QueryWrapper<SmartcityInstitution> wrapper = new QueryWrapper<>();
        for (int i = 0; i<IDList.size(); i++){
            wrapper.ne("kg_id", IDList.get(i));
            QueryWrapper<SmartcityInstitution> tmp = new QueryWrapper<>();
            tmp.eq("kg_id", IDList.get(i));
            SmartcityInstitution institution = institutionService.getOne(tmp);
            institutionList.add(institution);
        }
        if (IDList.size()<10){
            wrapper.last("limit " + String.valueOf(10-IDList.size()));
            institutionList.addAll(institutionService.list(wrapper));
        }
        return R.ok().data("items", institutionList);
    }

    @ApiOperation(value = "注册时，添加单位信息")
    @PostMapping("/addInstitutionByRegister")
    public Institution addInstitutionByRegister(@RequestBody Institution institution){
        SmartcityInstitution smartcityInstitution = new SmartcityInstitution();
        smartcityInstitution.setName(institution.getName());
        smartcityInstitution.setClassification(institution.getClassification());
        smartcityInstitution.setOrgaddress(institution.getOrgaddress());
        smartcityInstitution.setRegisteaddress(institution.getRegisteaddress());
        smartcityInstitution.setProvince(institution.getProvince());
        smartcityInstitution.setCity(institution.getCity());
        smartcityInstitution.setBusinessscope(institution.getBusinessscope());
        smartcityInstitution.setDomain(institution.getDomain());
        smartcityInstitution.setCreatetime(institution.getCreatetime());
        smartcityInstitution.setIndustry(institution.getIndustry());
        smartcityInstitution.setPhone(institution.getPhone());
        smartcityInstitution.setRegisteredcapital(institution.getRegisteredcapital());
        smartcityInstitution.setRegisterstatus(institution.getRegisterstatus());
        smartcityInstitution.setResearch(institution.getResearch());
        smartcityInstitution.setAvatar(institution.getAvatar());
        smartcityInstitution.setCode(institution.getCode());
        smartcityInstitution.setStatus(institution.getStatus());
        //System.out.println("SmartcityInstitutionController**************************");
        //System.out.println(smartcityInstitution);
        boolean save = institutionService.save(smartcityInstitution);
        if (save){
            institution.setId(smartcityInstitution.getId());
            return institution;
        }
        return null;
    }
}

