package com.smartcity.service.kgservice.controller;

import com.smartcity.service.kgservice.service.RelationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(description = "知识图谱关系服务")
@RestController
@RequestMapping("/kg/relation")
public class RelationController {

    @Autowired
    RelationService relationService;

    @GetMapping("/insert/{headID}/{tailID}/{relationType}")
    public String insertRelation(@PathVariable String headID, @PathVariable String tailID, @PathVariable String relationType){
        System.out.println("this is RelationController.insertRelation... headID, tailID and type are:(" + headID + ", " + tailID + ", " + relationType + ")" );
        return relationService.insertRelation(headID, tailID, relationType);
    }

    @GetMapping("/deleteByID/{relationID}")
    public int deleteRelationByID(@PathVariable String relationID){
        System.out.println("this is RelationController.deleteRelationByID... relationID is:(" + relationID + ")" );
        return relationService.deleteRelationByID(relationID);
    }

    @GetMapping("/deleteByHRT/{headID}/{tailID}/{relationType}")
    public int deleteRelationByHRT(@PathVariable String headID, @PathVariable String tailID, @PathVariable String relationType){
        System.out.println("this is RelationController.deleteRelationByHRT... headID, tailID and type are:(" + headID + ", " + tailID + ", " + relationType + ")" );
        return relationService.deleteRelationByHRT(headID, tailID, relationType);
    }

    @GetMapping("/findRelatedER/{entityID}")
    public Map<String, String> findRelationsByEntityID(@PathVariable String entityID){
        System.out.println("this is RelationController.findRelationsByEntityID... entityID is:(" + entityID + ")" );
        return relationService.findRelationsByEntityID(entityID);
    }
}
