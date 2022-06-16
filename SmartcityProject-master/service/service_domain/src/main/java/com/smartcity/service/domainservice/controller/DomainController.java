package com.smartcity.service.domainservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.common.commonutils.R;
import com.smartcity.service.domainservice.entity.Domain;
import com.smartcity.service.domainservice.entity.tree.FirstDomain;
import com.smartcity.service.domainservice.entity.tree.SecondDomain;
import com.smartcity.service.domainservice.service.DomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 领域类别 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-05
 */
@Api(description = "领域分类管理")
@RestController
@RequestMapping("/domainservice/domain")
public class DomainController {

    @Autowired
    private DomainService domainService;

    //通过excel来添加domain信息
    //获取上传文件
    @ApiOperation(value = "excel来添加领域信息")
    @PostMapping("/import")
    public R addPaperByExcel(MultipartFile file){
        domainService.saveByExcel(file, domainService);
        return R.ok();
    }

    //查询领域表所有数据
    //rest风格
    @ApiOperation(value = "查询所有领域分类")
    @GetMapping("/findAll")
    public R findAllDomain(){
        List<Domain> domainList = domainService.list(null);
        return R.ok().data("items", domainList);
    }

    //领域分类分层展示功能，树形结构
    //rest风格
    @ApiOperation(value = "领域分类分层展示")
    @GetMapping("/findAllDomainByTree")
    public R findAllDomainByTree(){

        //查询所有一级分类
        QueryWrapper<Domain> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("parent_id", "0");
        List<Domain> domainList1 = domainService.list(wrapper1);

        //查询所有二级分类
        QueryWrapper<Domain> wrapper2 = new QueryWrapper<>();
        wrapper2.ne("parent_id", "0");
        List<Domain> domainList2 = domainService.list(wrapper2);

        List<FirstDomain> tree = new ArrayList<>();

        //遍历所有一级分类，添加对应的二级分类到children中
        for(int i = 0; i < domainList1.size(); i++){
            Domain domain1 = domainList1.get(i);

            FirstDomain firstDomain = new FirstDomain();
            firstDomain.setId(domain1.getId());
            firstDomain.setTitle(domain1.getTitle());
            List<SecondDomain> children = new ArrayList<>();
            for(int j = 0; j < domainList2.size(); j++){
                Domain domain2 = domainList2.get(j);
                if(domain2.getParentId().equals(domain1.getId()) ){
                    children.add(new SecondDomain(domain2.getId(), domain2.getTitle()));
                }
            }
            firstDomain.setChildren(children);
            tree.add(firstDomain);
        }

        return R.ok().data("items", tree);
    }
}

