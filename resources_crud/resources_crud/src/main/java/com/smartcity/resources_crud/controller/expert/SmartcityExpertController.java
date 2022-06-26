package com.smartcity.resources_crud.controller.expert;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartcity.common.commonutils.JwtUtils;
import com.smartcity.common.commonutils.R;
import com.smartcity.common.commonutils.entity.CommonHistory;
import com.smartcity.common.commonutils.entity.Expert;
import com.smartcity.resources_crud.client.UserHistoryClient;
import com.smartcity.resources_crud.entity.expert.SmartcityExpert;
import com.smartcity.resources_crud.entity.expert.vo.ExpertQuery;
import com.smartcity.resources_crud.service.expert.impl.SmartcityExpertServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 专家 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-10-28
 */
//restcontroller返回json数据给前端，主要是因为@ResponseBody
/*@CrossOrigin//解决ajax跨域问题*/

@RestController
@RequestMapping("/expertservice/expert")
@Api(description = "专家管理")
public class SmartcityExpertController {

    @Autowired
    private SmartcityExpertServiceImpl expertService;

    @Autowired
    private UserHistoryClient historyClient;

    @ApiOperation(value = "根据excel导入数据")
    @PostMapping(value = "/import")
    public R addExpertByExcel(MultipartFile file){
        expertService.saveByExcel(file, expertService);
        return R.ok();
    }

    //查询专家表所有数据
    //rest风格
    @ApiOperation(value = "查询所有专家")
    @GetMapping("/findAll")
    public R findAllExpert(){
        List<SmartcityExpert> expertList = expertService.list(null);
        return R.ok().data("items", expertList);
    }

    @ApiOperation(value = "根据id,逻辑删除专家")
    @DeleteMapping("remove/{id}")
    public R removeById(
            @ApiParam(name = "id",value = "专家id",required = true)
            @PathVariable String id){
        boolean flag = expertService.removeById(id);
        if(flag){
            return R.ok();
        }else{
            return R.error().message("删除失败，请稍后重试");
        }
    }


    //分页查询专家列表
    //page：当前页码，limit：每页显示数据条数
    @ApiOperation(value = "分页专家列表")
    @GetMapping("pageExpert/{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit){

        Page<SmartcityExpert> pageParam = new Page<>(page, limit);

        expertService.page(pageParam, null);
        List<SmartcityExpert> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        Map map = new HashMap();
        map.put("total", total);
        map.put("rows", records);
        return R.ok().data(map);
        //return  R.ok().data("total", total).data("rows", records);
    }

    //条件分页查询
    //使用@RequestBody，接收前端json数据，需要用post
    @ApiOperation(value = "条件分页专家列表")
    @PostMapping("pageExpertCondition/{page}/{limit}")
    public R pageExpertCondition(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "condition", value = "条件", required = false)
            @RequestBody(required = false) ExpertQuery expertQuery){
        //创建page对象
        Page<SmartcityExpert> pageParam = new Page<>(page, limit);
        //构建条件wrapper
        QueryWrapper<SmartcityExpert> wrapper = new QueryWrapper<>();
        //多条件组合查询
        //判断条件值是否为空，如果不为空则拼接条件
        String name = expertQuery.getName();
        String institution = expertQuery.getInstitution();
        String intro = expertQuery.getIntro();
        String begin = expertQuery.getBegin();
        String end = expertQuery.getEnd();
        if(!StringUtils.isEmpty(name)){
            wrapper.like("name", name);
        }
        if(!StringUtils.isEmpty(institution)){
            wrapper.like("institution", institution);
        }
        if(!StringUtils.isEmpty(intro)){
            wrapper.like("intro", intro);
        }
        if(!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create", begin);
        }
        if(!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create", end);
        }
        //排序
        wrapper.orderByDesc("gmt_create");
        //调用方法实现条件查询
        expertService.page(pageParam, wrapper);
        List<SmartcityExpert> records = pageParam.getRecords();
        long total = pageParam.getTotal();

        return  R.ok().data("total", total).data("rows", records);
    }

    //添加专家
    //使用@RequestBody，接收前端json数据，需要用post
    @ApiOperation(value = "添加专家")
    @PostMapping("/addExpert")
    public R addExpert(
            @ApiParam(name = "expert", value = "专家", required = true)
            @RequestBody(required = true) SmartcityExpert expert){

        //调用方法实现添加
        boolean save = expertService.save(expert);
        if(save){
            return R.ok().data("expert",expert);
        }
        else{
            return R.error().message("添加失败，请稍后重试");
        }
    }

    //根据ID查询专家
    @ApiOperation(value = "ID查询专家")
    @GetMapping("/getExpert/{id}")
    public R getExpertById(
            @ApiParam(name = "id", value = "专家ID", required = true)
            @PathVariable String id,
            HttpServletRequest request){
        //调用方法实现查询
        SmartcityExpert expert = expertService.getById(id);

        if (request.getHeader("admin") != null){
            return R.ok().data("expert", expert);
        }

        //调用jwt工具类，根据request获取头信息，返回用户id,不是user_id
        String logId = JwtUtils.getMemberIdByJwtToken(request);
        CommonHistory history = new CommonHistory();
        history.setLogId(logId);
        history.setResourceType(1);
        history.setKgId(expert.getKgId());
        boolean saveHistory = historyClient.saveHistory(history);
        if (saveHistory)
            return R.ok().data("expert", expert);
        else
            return R.ok().message("添加浏览记录失败").data("expert", expert);
    }

    //修改专家
    //使用@RequestBody，接收前端json数据，需要用post
    @ApiOperation(value = "修改专家")
    @PostMapping("/updateExpert")
    public R updateExpert(
            @ApiParam(name = "expert", value = "专家", required = true)
            @RequestBody(required = true) SmartcityExpert expert){
//        try {
//            int a=10/0;
//        }catch(Exception e){
//            throw  new CustomException(444,"自定义异常类处理");
//        }
        //调用方法实现修改
        boolean update = expertService.updateById(expert);
        if(update){
            return R.ok().data("expert", expert);
        }
        else{
            return R.error().message("更新专家信息失败，请稍后重试");
        }
    }

    //根据ID获取专家batch
    @ApiOperation(value = "根据ID获取专家batch")
    @GetMapping("/getExpertBatch")
    public R getBatch(@RequestParam("IDList") List<String> IDList){
        //System.out.println(IDList);
        List<SmartcityExpert> expertList = new ArrayList<>();
        QueryWrapper<SmartcityExpert> wrapper = new QueryWrapper<>();
        for (int i = 0; i<IDList.size(); i++){
            wrapper.ne("kg_id", IDList.get(i));
            QueryWrapper<SmartcityExpert> tmp = new QueryWrapper<>();
            tmp.eq("kg_id", IDList.get(i));
            SmartcityExpert expert = expertService.getOne(tmp);
            expertList.add(expert);
        }
        if (IDList.size()<10){
            wrapper.last("limit "+String.valueOf(10-IDList.size()));
            expertList.addAll(expertService.list(wrapper));
        }
        return R.ok().data("items", expertList);
    }

    //注册时，添加专家信息
    @ApiOperation(value = "注册时，添加专家信息")
    @PostMapping("/addExpertByRegister")
    public Expert addExpertByRegister(@RequestBody Expert expert){
        SmartcityExpert smartcityExpert = new SmartcityExpert();
        smartcityExpert.setName(expert.getName());
        smartcityExpert.setIntro(expert.getIntro());
        smartcityExpert.setCareer(expert.getCareer());
        smartcityExpert.setDomain(expert.getDomain());
        smartcityExpert.setEmail(expert.getEmail());
        smartcityExpert.setPhone(expert.getPhone());
        smartcityExpert.setFax(expert.getFax());
        smartcityExpert.setAvatar(expert.getAvatar());
        boolean save = expertService.save(smartcityExpert);
        if (save){
            expert.setId(smartcityExpert.getId());
            return expert;
        }
        return null;
    }
}

