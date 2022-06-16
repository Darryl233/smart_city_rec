package com.smartcity.service.rs.service.impl;

import com.smartcity.service.rs.entity.HNSW.HNSWIndex;
import com.smartcity.service.rs.entity.HNSW.HNSWNode;
import com.smartcity.service.rs.entity.HNSW.IndexService;
import com.smartcity.service.rs.client.impl.RedisClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HNSWService implements IndexService {
    private HNSWIndex expertIndex;
    private HNSWIndex institutionIndex;
    private HNSWIndex caseIndex;
    private HNSWIndex patentIndex;
    private HNSWIndex paperIndex;
    private HNSWIndex solutionIndex;
    private HNSWIndex requirementIndex;
    private HNSWIndex achievementIndex;

    private RedisClient redisClient;

    private static final HashSet<String> relationIDs = new HashSet<>(Arrays.asList("1&2", "2&5", "1&7", "2&3", "3&4", "1&6", "1&8", "2&4", "history"));
    private ArrayList<double[]> relationEmbeddings = new ArrayList<>();

    public HNSWService() {
        this.redisClient = RedisClient.getInstance();

        this.reload();
    }

    // "资源类型,1专家，2单位,3需求,4方案,5案例,6成果,7论文,8专利"
    @Override
    public boolean addItem(HNSWNode newNode, int type) {
        switch (type) {
            case 1: return expertIndex.addItem(newNode);
            case 2: return institutionIndex.addItem(newNode);
            case 3: return requirementIndex.addItem(newNode);
            case 4: return solutionIndex.addItem(newNode);
            case 5: return caseIndex.addItem(newNode);
            case 6: return achievementIndex.addItem(newNode);
            case 7: return paperIndex.addItem(newNode);
            case 8: return patentIndex.addItem(newNode);
        }
        return false;
    }

    @Override
    public boolean delItem(HNSWNode newNode, int type) {
        switch (type) {
            case 1: return expertIndex.delItem(newNode);
            case 2: return institutionIndex.delItem(newNode);
            case 3: return requirementIndex.delItem(newNode);
            case 4: return solutionIndex.delItem(newNode);
            case 5: return caseIndex.delItem(newNode);
            case 6: return achievementIndex.delItem(newNode);
            case 7: return paperIndex.delItem(newNode);
            case 8: return patentIndex.delItem(newNode);
        }
        return false;
    }

//    public ArrayList<HNSWNode> search(HNSWNode node, int topk) {
//        return myIndex.search(node, topk);
//    }

    public ArrayList<HNSWNode> search(double[] vec, int type, int topk) {

        HNSWNode qnode = new HNSWNode("query", vec);
        switch (type) {
            case 1: return expertIndex.search(qnode, topk);
            case 2: return institutionIndex.search(qnode, topk);
            case 3: return requirementIndex.search(qnode, topk);
            case 4: return solutionIndex.search(qnode, topk);
            case 5: return caseIndex.search(qnode, topk);
            case 6: return achievementIndex.search(qnode, topk);
            case 7: return paperIndex.search(qnode, topk);
            case 8: return patentIndex.search(qnode, topk);
        }
        return null;
    }

    // "资源类型,1专家，2单位,3需求,4方案,5案例,6成果,7论文,8专利"
    public void reload() {
        HNSWIndex expertIndex = new HNSWIndex(80, 64);
        HNSWIndex institutionIndex = new HNSWIndex(80, 64);
        HNSWIndex caseIndex = new HNSWIndex(80, 64);
        HNSWIndex patentIndex = new HNSWIndex(80, 64);
        HNSWIndex paperIndex = new HNSWIndex(80, 64);
        HNSWIndex solutionIndex = new HNSWIndex(80, 64);
        HNSWIndex requirementIndex = new HNSWIndex(80, 64);
        HNSWIndex achievementIndex = new HNSWIndex(80, 64);
        HashMap<String, Vector<Double>> embds = this.redisClient.loadEmbeddings();
        this.relationEmbeddings = new ArrayList<>();
        for(String relation: relationIDs) {
            double[] rembd = this.redisClient.getEmbedding(relation);
            if (rembd != null) {
                this.relationEmbeddings.add(rembd);
            }
        }
        expertIndex.setRelationEmbeddings(relationEmbeddings);
        institutionIndex.setRelationEmbeddings(relationEmbeddings);
        caseIndex.setRelationEmbeddings(relationEmbeddings);
        patentIndex.setRelationEmbeddings(relationEmbeddings);
        paperIndex.setRelationEmbeddings(relationEmbeddings);
        solutionIndex.setRelationEmbeddings(relationEmbeddings);
        requirementIndex.setRelationEmbeddings(relationEmbeddings);
        achievementIndex.setRelationEmbeddings(relationEmbeddings);
        System.out.println("start init HNSW");
        int cnt = 0;
        System.out.println(embds.size());
        for (String key: embds.keySet()) {
            cnt += 1;
            if (cnt % 1000 == 0)
                System.out.println(cnt);
            Vector<Double> embd = embds.get(key);
            String[] key_types = key.split("_");
            double[] vec2array = new double[expertIndex.getDIMENSION()];
            for(int i = 0; i < expertIndex.getDIMENSION(); i++) {
                vec2array[i] = embd.get(i);
            }
            int type = 0;
            switch (key_types[0]) {
                case "expe": {
                    type = 1;
                    break;
                }
                case "institution": {
                    type = 2;
                    break;
                }
                case "requ": {
                    type = 3;
                    break;
                }
                case "solu": {
                    type = 4;
                    break;
                }
                case "case": {
                    type = 5;
                    break;
                }
                case "achi": {
                    type = 6;
                    break;
                }
                case "pape": {
                    type = 7;
                    break;
                }
                case "pate": {
                    type = 8;
                    break;
                }
                default: {
                    System.out.println("key不匹配8类资源" + key);
                }
            }


            HNSWNode node = new HNSWNode(key, vec2array);
            switch (type) {
                case 1: expertIndex.addItem(node); break;
                case 2: institutionIndex.addItem(node); break;
                case 3: requirementIndex.addItem(node); break;
                case 4: solutionIndex.addItem(node); break;
                case 5: caseIndex.addItem(node); break;
                case 6: achievementIndex.addItem(node); break;
                case 7: paperIndex.addItem(node); break;
                case 8: patentIndex.addItem(node); break;
            }
        }
        this.expertIndex = expertIndex;
        this.institutionIndex = institutionIndex;
        this.caseIndex = caseIndex;
        this.patentIndex = patentIndex;
        this.paperIndex = paperIndex;
        this.solutionIndex = solutionIndex;
        this.requirementIndex = requirementIndex;
        this.achievementIndex = achievementIndex;
        System.out.println("HNSW reload completed");
    }

}
