package com.smartcity.service.relationservice.controller;


import com.smartcity.common.commonutils.R;
import com.smartcity.service.relationservice.service.RelationsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 关系表：单位-提出-案例 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-12-28
 */
@Api(description = "关系管理")
@RestController
@RequestMapping("/relationservice/relations")
public class RelationsController {
    @Autowired
    private RelationsService relationsService;

    @PostMapping("/import")
    public R addRelationsByExcel(MultipartFile file){
        relationsService.saveByExcel(file, relationsService);
        return R.ok();
    }
}

