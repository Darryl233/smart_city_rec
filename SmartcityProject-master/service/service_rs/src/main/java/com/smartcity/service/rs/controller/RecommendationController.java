package com.smartcity.service.rs.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartcity.common.commonutils.JwtUtils;
import com.smartcity.common.commonutils.R;
import com.smartcity.service.rs.client.impl.RedisClient;
import com.smartcity.service.rs.entity.HNSW.HNSWNode;
import com.smartcity.service.rs.client.*;
import com.smartcity.service.rs.entity.Recommendation;
import com.smartcity.service.rs.schedule.UserRecommender;
import com.smartcity.service.rs.service.RecommendationService;
import com.smartcity.service.rs.service.impl.HNSWService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.*;
import com.alibaba.fastjson.JSON;

/**
 * <p>
 * 推荐 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-06
 */
@Api(description = "推荐服务")
@RestController
@RequestMapping("/rs/recommendation")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private ExpertClient expertClient;

    @Autowired
    private InstitutionClient institutionClient;

    @Autowired
    private RequirementClient requirementClient;

    @Autowired
    private SolutionClient solutionClient;

    @Autowired
    private CaseClient caseClient;

    @Autowired
    private AchievementClient achievementClient;

    @Autowired
    private PaperClient paperClient;

    @Autowired
    private PatentClient patentClient;

    @Autowired
    private MCRecClient mcRecClient;

    @Autowired
    private HNSWService hnswService;

    @Autowired
    private UserRecommender userRecommender;


    @ApiOperation(value = "(没有使用的接口)按资源类型给专家用户进行推荐")
    @PostMapping("/postExpertRS/{type}")
    public R postExpertRS( @ApiParam(name = "type", value = "资源类型,1专家，2单位,3需求,4方案,5案例,6成果,7论文,8专利", required = true)
                                @PathVariable Integer type,
                           @ApiParam(name = "RSId", value = "资源的ID", required = true)
                                @RequestParam(value = "RSId")  String RSId,
                           HttpServletRequest request
                           ){
        //调用jwt工具类，根据request获取头信息，返回用户id,不是user_id
        String logId = JwtUtils.getMemberIdByJwtToken(request);

        Recommendation recommendation = new Recommendation();
        recommendation.setLogId(logId);
        recommendation.setType(type);
        recommendation.setItemId(RSId);
        recommendationService.save(recommendation);

        return R.ok().data("recommendation", recommendation);
    }

    @GetMapping("/test_redis/{id}")
    public R test(@PathVariable String id) {
        userRecommender.recommend();
        return R.ok();
    }


    @ApiOperation(value = "按资源类型给专家用户进行推荐")
    @GetMapping("/getRS/{type}")
    public R getRs(
            @ApiParam(name = "type", value = "资源类型,1专家，2单位,3需求,4方案,5案例,6成果,7论文,8专利", required = true)
            @PathVariable Integer type,
            HttpServletRequest request){
        System.out.println(request.getQueryString());
        //调用jwt工具类，根据request获取头信息，返回用户id,不是user_id
        String logId = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println(logId == "");  //没登录用户为：True

        HashMap<String, Float> resultMap = new HashMap<>();
        List<String> IDList = new ArrayList<>();

        System.out.println("*********logID: "+logId);
        if (!logId.equals("")) {
            QueryWrapper<Recommendation> wrapper = new QueryWrapper<>();
            wrapper.eq("log_id", logId);
            wrapper.eq("type", type);
            List<Recommendation> recommendationList = recommendationService.list(wrapper);
            Collections.reverse(recommendationList);
            System.out.println("cf result: " + recommendationList.toString());
            double[] queryEmebdding = null;
            if (!recommendationList.isEmpty()) {
                Recommendation bestOne = recommendationList.get(0);
                queryEmebdding = RedisClient.getInstance().getEmbedding(bestOne.getItemId());
            }
            if (queryEmebdding == null)
                queryEmebdding = RedisClient.getInstance().getEmbedding(logId);
            ArrayList<String> itemList = new ArrayList<>();
            if (queryEmebdding == null) {
                queryEmebdding = new double[64];
            }
            System.out.println(queryEmebdding);
            ArrayList<HNSWNode> nodes = hnswService.search(queryEmebdding, type, 10);

            for (HNSWNode node : nodes) {
//                ArrayList<HNSWNode> hnswItems = hnswService.search(node.getData(), type, 10);
//                for (HNSWNode itm: hnswItems) {
                    itemList.add(node.getLabel());
//                }
            }
            System.out.println("HNSW res: " + itemList.toString());
            for (Recommendation recommendation : recommendationList) {
                itemList.add(recommendation.getItemId());
            }
            ArrayList<String> MCRecResult = new ArrayList<>();
            try {
                MCRecResult = mcRecClient.getRecommendation(logId, itemList, type);

            } catch (Exception ignored) {
            }
            if (!MCRecResult.isEmpty())
                IDList = MCRecResult;
            else {
                for (Recommendation recommendation: recommendationList) {
                    if (IDList.size() > 5)
                        break;
                    IDList.add(recommendation.getItemId());
                }
                for (String itm: itemList) {
                    if (IDList.size() > 10)
                        break;
                    IDList.add(itm);
                }
            }
            System.out.println(MCRecResult);
        }
//            System.out.println(MCRecResult);
//
//            Type mapOfStringObjectType = new TypeToken<Map<Integer, List<List<Object>>>>() {}.getType();
//            Gson gson = new Gson();
//            Map<Integer, List<List<Object>>> obj = gson.fromJson(MCRecResult, mapOfStringObjectType);
//            if (obj.isEmpty()){
//                break out;
//            }
//            List<List<Object>> lists = obj.get(type);
//
//            for (Recommendation recommendation : recommendationList) {
//                String itemId = recommendation.getItemId();
//                Float score = recommendation.getScore();
//                resultMap.put(itemId, (float) (score*8.0));
//            }
//
//            for (int i=0; i< lists.size(); i++){
//                String itemId = lists.get(i).get(0).toString();
//                float score = Float.parseFloat(lists.get(i).get(1).toString());
//                if (resultMap.containsKey(itemId)){
//                    resultMap.put(itemId, resultMap.get(itemId) + score);
//                }
//                else{
//                    resultMap.put(itemId, score);
//                }
//            }
//
//            //对result进行排序
//            List<Map.Entry<String, Float>> resultList =
//                    new ArrayList<Map.Entry<String, Float>>(resultMap.entrySet());
//            Collections.sort(resultList, new Comparator<Map.Entry<String, Float>>() {
//                public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
//                    if (o2.getValue() > o1.getValue()){
//                        return 1;
//                    }
//                    else if (o2.getValue() == o1.getValue())
//                        return 0;
//                    else
//                        return -1;
//                }
//            });
//
//            //System.out.println("**************\n 查表 推荐");
//            for (int j=0; j < resultList.size(); j++) {
//                if (j >= 10) break;
//                IDList.add(resultList.get(j).getKey());
//            }
//            System.out.println(IDList);
//        } else {
//            IDList.add("expe_40");
//            IDList.add("expe_69");
//            IDList.add("expe_22");
//            IDList.add("expe_43");
//            IDList.add("expe_70");
////            IDList.add("1347025125924442113");
////            IDList.add("1347025126125768706");
////            IDList.add("1347025126343872513");
////            IDList.add("1347025126503256066");
////            IDList.add("1347025126993989633");
//        }
//        System.out.println(IDList);   e

        if(type == 1){
            //推荐资源类型为专家
            R expertBatch = expertClient.getBatch(IDList);
            return expertBatch;
        }else if(type == 2) {
            //推荐资源类型为单位
            R institutionBatch = institutionClient.getBatch(IDList);
            return institutionBatch;
        }else if (type == 3) {
            //推荐资源类型为需求
            R requirementBatch = requirementClient.getBatch(IDList);
            return requirementBatch;
        }else if (type == 4) {
            //推荐资源类型为方案
            R solutionBatch = solutionClient.getBatch(IDList);
            return solutionBatch;
        }else if (type == 5) {
            //推荐资源类型为案例
            R caseBatch = caseClient.getBatch(IDList);
            return caseBatch;
        }else if (type == 6) {
            //推荐资源类型为成果
            R achievementBatch = achievementClient.getBatch(IDList);
            return achievementBatch;
        }else if (type == 7) {
            //推荐资源类型为论文
            R paperBatch = paperClient.getBatch(IDList);
            return paperBatch;
        }else if (type == 8) {
            //推荐资源类型为专利
            R patentBatch = patentClient.getBatch(IDList);
            return patentBatch;
        }
        return R.error();
    }


    @Scheduled(cron = "0 0 12 * * ?")  //每6h执行1次
    private void  reloadHNSW() {
        hnswService.reload();
    }
}

