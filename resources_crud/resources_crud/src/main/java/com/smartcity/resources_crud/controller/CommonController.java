package com.smartcity.resources_crud.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.common.commonutils.BlockChainController;
import com.smartcity.common.commonutils.JwtUtils;
import com.smartcity.common.commonutils.R;
import com.smartcity.common.commonutils.entity.BlockChainResource;
import com.smartcity.common.commonutils.entity.CommonHistory;
import com.smartcity.resources_crud.client.UserHistoryClient;
import com.smartcity.resources_crud.entity.BaseEntity;
import com.smartcity.resources_crud.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommonController<E extends BaseEntity> implements BaseController<E>{

    @Autowired
    private ResourceService<E> service;

    @Autowired
    private UserHistoryClient historyClient;

//    public CommonController(ResourceService<E> service) {
//        this.service = service;
//    }


    @Override
    @PostMapping("/import")
    public R addByExcel(MultipartFile file) {
        service.saveByExcel(file, service);
        return R.ok();
    }

    @Override
    @GetMapping("/findAll")
    public R findAll() {
        List<E> EList = service.list(null);
        return R.ok().data("items", EList);
    }

    @Override
    @DeleteMapping("/remove/{id}")
    public R removeById(@PathVariable String id) {
        boolean flag = service.removeById(id);
        if(flag){
            return R.ok();
        }else{
            return R.error();
        }
    }

    @Override
    @PostMapping("/add{resourceName}")
    public R addObject(@RequestBody(required = true) E entity,
                       @PathVariable String resourceName,
                       HttpServletRequest request) {
        if (!Objects.equals(resourceName, "Achievement"))
            return R.error();
        boolean save = service.save(entity);
        if(save){
            if (request.getHeader("admin")!=null){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                BlockChainResource resource = new BlockChainResource("0", "20", "null",
                        "Achievement_"+entity.getId(), formatter.format(entity.getGmtCreate()), "Admin_1");
                BlockChainController.setBlockChainResource(resource);
            }
            return R.ok().data("entity", entity);
        }
        else{
            return R.error();
        }
    }

    @Override
    @GetMapping("/get{resourceName}/{id}")
    public R getObject(@PathVariable String id,
                       @PathVariable String resourceName,
                       HttpServletRequest request) {

        if (!Objects.equals(resourceName, "Achievement"))
            return R.error();
        E entity = service.getById(id);
        System.out.println(entity);
        if (request.getHeader("admin") != null){
            return R.ok().data("entity", entity);
        }

        //调用jwt工具类，根据request获取头信息，返回用户id,不是user_id
        String logId = JwtUtils.getMemberIdByJwtToken(request);
        CommonHistory history = new CommonHistory();
        history.setLogId(logId);
        history.setResourceType(6);
        history.setKgId(entity.getKgId());
        boolean saveHistory = historyClient.saveHistory(history);
        if (saveHistory)
            return R.ok().data("entity", entity);
        else
            return R.ok().message("添加浏览记录失败").data("entity", entity);
    }

    @Override
    @PostMapping("/update{resourceName}")
    public R update( @RequestBody(required = true) E entity, @PathVariable String resourceName) {
        if (!Objects.equals(resourceName, "Achievement"))
            return R.error();
        boolean update = service.updateById(entity);
        if(update){
            return R.ok().data("entity", entity);
        }
        else{
            return R.error();
        }
    }

    @Override
    @GetMapping("/get{resourceName}Batch")
    public R getBatch(@RequestParam("IDList") List<String> IDList, @PathVariable String resourceName) {
        System.out.println("hello world!");
        List<E> entityList = new ArrayList<>();
        QueryWrapper<E> wrapper = new QueryWrapper<>();
        for(int i=0; i<IDList.size(); i++){
            wrapper.ne("kg_id", IDList.get(i));
            QueryWrapper<E> tmp = new QueryWrapper<>();
            tmp.eq("kg_id", IDList.get(i));
            E achievement = service.getOne(tmp);
            entityList.add(achievement);
        }
        if (IDList.size()<10){
            wrapper.last("limit "+String.valueOf(10-IDList.size()));
            entityList.addAll(service.list(wrapper));
        }
        return R.ok().data("items", entityList);
    }
}
