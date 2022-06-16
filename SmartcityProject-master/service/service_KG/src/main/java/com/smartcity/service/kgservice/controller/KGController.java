package com.smartcity.service.kgservice.controller;

import com.smartcity.common.commonutils.R;
import com.smartcity.service.kgservice.entity.Edge;
import com.smartcity.service.kgservice.entity.KGInstitution;
import com.smartcity.service.kgservice.entity.Node;
import com.smartcity.service.kgservice.service.InstitutionService;
import com.smartcity.service.kgservice.service.ExpertService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Api(description = "知识图谱服务")
@RestController
@RequestMapping("/kg")
public class KGController {

    @Autowired
    private InstitutionService institutionService;

    @Autowired
    private ExpertService expertService;
    
    @GetMapping("/findSubKGByInstitutionId/{institutionId}")
    public R findSubKGByInstitutionId(@PathVariable String institutionId){
        List<String[]> subGraphs = institutionService.extractSubGraph(institutionId);
        List<Node> nodeList = new ArrayList<>();
        List<Edge> edgeList = new ArrayList<>();
        HashMap<String, Integer> node2id = new HashMap<>();
        int count = 0;
        for (String [] subGraph: subGraphs){
            if (!node2id.containsKey(subGraph[0])){
                node2id.put(subGraph[0], count);
                nodeList.add(new Node(count, subGraph[1], subGraph[0].substring(0, 4)));
                count += 1;
            }
            if (!node2id.containsKey(subGraph[3])){
                node2id.put(subGraph[3], count);
                nodeList.add(new Node(count, subGraph[4], subGraph[3].substring(0, 4)));
                count += 1;
            }
            edgeList.add(new Edge(node2id.get(subGraph[0]), node2id.get(subGraph[3]), subGraph[2]));
        }
        return R.ok().data("nodes", nodeList).data("edges", edgeList);
    }
}
