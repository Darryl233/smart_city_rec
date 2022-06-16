package com.smartcity.service.kgservice.controller;

import com.alibaba.fastjson.JSON;
import com.smartcity.service.kgservice.entity.KGExpert;
import com.smartcity.service.kgservice.service.ExpertService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(description = "知识图谱专家服务")
@RestController
@RequestMapping("/kg/expert")
public class ExpertController {

    @Autowired
    private ExpertService expertService;

    @GetMapping("/id/{expertId}")
    public KGExpert findExpertById(@PathVariable String expertId){
        System.out.println("this is ExpertController.findExpertById... id = " + expertId);
        return expertService.findExpertById(expertId);
    }

    @GetMapping("/name/{expertName}")
    public List<KGExpert> findExpertByName(@PathVariable String expertName){
        System.out.println("this is ExpertController.findExpertByName... name = " + expertName);
        return expertService.findExpertByName(expertName);
    }

    @GetMapping("/fuzzyName/{expertName}")
    public List<KGExpert> findExpertsByFuzzyName(@PathVariable String expertName){
        System.out.println("this is ExpertController.findExpertsByFuzzyName... name = " + expertName);
        return expertService.findExpertsByFuzzyName(expertName);
    }

    @GetMapping("/institutionId/{institutionId}")
    public List<KGExpert> findExpertsByInstitutionId(@PathVariable String institutionId){
        System.out.println("this is ExpertController.findExpertsByInstitutionId... id = " + institutionId);
        return expertService.findExpertsByInstitutionId(institutionId);
    }

    @GetMapping("/institutionName/{institutionName}")
    public List<KGExpert> findExpertsByInstitutionName(@PathVariable String institutionName) {
        System.out.println("this is ExpertController.findExpertsByInstitutionName... name = " + institutionName);
        return expertService.findExpertsByInstitutionName(institutionName);
    }

    @GetMapping("/delete/{expertId}")
    public KGExpert deleteExpert(@PathVariable String expertId){
        System.out.println("this is ExpertController.deleteExpert... id = " + expertId);
        return expertService.deleteExpert(expertId);
    }

    //RequestParam
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public KGExpert updateExpert(@RequestBody String paramsJson){
        Map<String, Object> paramMap = JSON.parseObject(paramsJson);
        String expertId = (String)paramMap.get("kg_id");
        paramMap.remove("kg_id");
        System.out.println("this is ExpertController.updateExpert... id = " + expertId);
        return expertService.updateExpert(expertId, paramMap);
    }

    @RequestMapping(value="/insert", method = RequestMethod.POST)
    public KGExpert insertExpert(@RequestBody String paramsJson){
        Map<String, Object> paramMap = JSON.parseObject(paramsJson);
        String expertId = (String)paramMap.get("kg_id");
        paramMap.remove("kg_id");
        System.out.println("this is ExpertController.insertExpert... id = " + expertId);
        return expertService.insertExpert(expertId, paramMap);
    }

    @GetMapping("/extract/{expertId}")
    public List<String []> extractSubGraph(@PathVariable String expertId){
        System.out.println("this is ExpertController.extractSubGraph... id = " + expertId);
        return expertService.extractSubGraph(expertId);
    }
}
