package com.smartcity.service.kgservice.controller;

import com.alibaba.fastjson.JSON;
import com.smartcity.common.commonutils.R;
import com.smartcity.service.kgservice.entity.KGInstitution;
import com.smartcity.service.kgservice.entity.Node;
import com.smartcity.service.kgservice.service.InstitutionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(description = "知识图谱单位服务")
@RestController
@RequestMapping("/kg/institution")
public class InstitutionController {

    @Autowired
    private InstitutionService institutionService;

    @GetMapping("/name_or_id/{name}")
    public KGInstitution findInstitutionByNameOrId(@PathVariable String name){
        System.out.println("this is InstitutionController.findInstitutionByNameOrId... name = " + name);
        return institutionService.findInstitutionByNameOrId(name);
    }

    @GetMapping("/fuzzyName/{name}")
    public List<KGInstitution> findInstitutionsByFuzzyName(@PathVariable String name){
        System.out.println("this is InstitutionController.findInstitutionsByFuzzyName... name = " + name);
        return institutionService.findInstitutionsByFuzzyName(name);
    }

    @GetMapping("/by_employ/{expertId}")
    public KGInstitution findInstitutionByExpert(@PathVariable String expertId){
        System.out.println("this is InstitutionController.findInstitutionByExpert... expertId = " + expertId);
        return institutionService.findInstitutionByExpertId(expertId);
    }

    @GetMapping("/delete/{institutionId}")
    public KGInstitution deleteInstitution(@PathVariable String institutionId){
        System.out.println("this is InstitutionController.deleteInstitution... institutionId = " + institutionId);
        return institutionService.deleteInstitution(institutionId);
    }

    //RequestParam
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public KGInstitution updateInstitution(@RequestBody String paramsJson){
        Map<String, Object> paramMap = JSON.parseObject(paramsJson);
        String institutionId = (String)paramMap.get("kg_id");
        paramMap.remove("kg_id");
        System.out.println("this is InstitutionController.updateInstitution... institutionId = " + institutionId);
        return institutionService.updateInstitution(institutionId, paramMap);
    }

    @RequestMapping(value="/insert", method = RequestMethod.POST)
    public KGInstitution insertInstitution(@RequestBody String paramsJson){
        Map<String, Object> paramMap = JSON.parseObject(paramsJson);
        String institutionId = (String)paramMap.get("kg_id");
        paramMap.remove("kg_id");
        System.out.println("this is InstitutionController.insertInstitution... institutionId = " + institutionId);
        return institutionService.insertInstitution(institutionId, paramMap);
    }

    @GetMapping("/extract/{institutionId}")
    public List<String []> extractSubGraph(@PathVariable String institutionId){
        System.out.println("this is InstitutionController.extractSubGraph... institutionId = " + institutionId);
        return institutionService.extractSubGraph(institutionId);
    }

}
