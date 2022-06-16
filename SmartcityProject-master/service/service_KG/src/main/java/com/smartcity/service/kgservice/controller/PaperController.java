package com.smartcity.service.kgservice.controller;

import com.smartcity.service.kgservice.entity.MyEntity;
import com.smartcity.service.kgservice.service.PaperService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "知识图谱论文服务")
@RestController
@RequestMapping("/kg/paper")
public class PaperController {

    @Autowired
    PaperService paperService;

    @GetMapping("/id/{paperID}")
    public MyEntity findPaperById(@PathVariable String paperID){
        System.out.println("this is PaperController.findPaperById... id = " + paperID);
        return paperService.findPaperById(paperID);
    }
    @GetMapping("/title/{paperTitle}")
    public List<MyEntity> findPaperByTitle(@PathVariable String paperTitle){
        System.out.println("this is PaperController.findPaperByTitle... title = " + paperTitle);
        return paperService.findPaperByTitle(paperTitle);
    }
}
