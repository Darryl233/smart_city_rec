package com.smartcity.service.rs.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.common.commonutils.entity.CommonHistory;
import com.smartcity.service.rs.analyzer.CollaborativeFilteringAnalyzer;
import com.smartcity.service.rs.client.UserHistoryClient;
import com.smartcity.service.rs.entity.Recommendation;
import com.smartcity.service.rs.service.RecommendationService;
import com.smartcity.service.rs.service.impl.HNSWService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class UserRecommender {
    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private UserHistoryClient historyClient;

    /**
     * 将 logid resourcetype resourceid 转化为 logid ： resourcetype： resourceidList
     */
    public HashMap<String, HashMap<Integer, List<String>>> dataFormat(){
        HashMap<String, HashMap<Integer, List<String>>> lists = new HashMap<>();
        List<CommonHistory> allHistory = historyClient.getAllHistory();
        for (CommonHistory history : allHistory){
            if( !lists.containsKey(history.getLogId()) ){
                lists.put(history.getLogId(), new HashMap<>());
                for (int i=1; i < 9; i++){
                    lists.get(history.getLogId()).put(i, new ArrayList<>());
                }
            }
            lists.get(history.getLogId()).get(history.getResourceType()).add(history.getKgId());
        }
        return lists;
    }

//    @Scheduled(cron = "0 0/1 * * * ?")  //每分钟执行1次
    @Scheduled(cron = "0 0 0 * * ?")  //每天00:00执行1次
    public void recommend(){
        //将数据库中的data进行格式转换，map格式为 logid: resourcetype: resourceidList
        UserRecommender userRecommender = new UserRecommender();
        HashMap<String, HashMap<Integer, List<String>>> map = dataFormat();
        System.out.println("*************** 数据格式转换 **************");
        /*
        for (String userId : map.keySet()){
            System.out.println(map.get(userId));
        }
        */
        //创建一个协同过滤算法类
        CollaborativeFilteringAnalyzer analyzer = new CollaborativeFilteringAnalyzer();
        //获取用户之间的相似度, 格式为 <UserA_id, <UserB_id, score > >
        HashMap<String, HashMap<String, Float>> userSimilarity = analyzer.userSimilarityConsine(map);
        System.out.println("*************** 计算用户之间相似度 **************");
        /*
        for (String userId : userSimilarity.keySet()){
            System.out.println(userSimilarity.get(userId));
        }
        */

        //根据用户的logId，计算各类型资源的推荐度
        for (String userId : map.keySet()){
            //根据LogId获取相关物品的所有推荐度
            HashMap<Integer, HashMap<String, Float>> scoreMap = analyzer.getRecommendScore(map, userSimilarity, userId);
            HashMap<Integer, HashMap<String, Float>> sortRecommend = analyzer.sortRecommend(scoreMap, 10);
            System.out.println("*************** 计算用户Logid为 "+userId+" 的所有物品推荐度 **************");
            for (int type=1; type<9; type++){
                //System.out.println("资源类型type为 "+ type);
                //System.out.println(sortRecommend.get(type));
                if (sortRecommend.get(type) == null){
                    continue;
                }
                for (String i: sortRecommend.get(type).keySet()){
                    QueryWrapper<Recommendation> wrapper = new QueryWrapper<>();
                    wrapper.eq("log_id", userId);
                    wrapper.eq("type", type);
                    wrapper.eq("item_id", i);
                    Recommendation recommendation = recommendationService.getOne(wrapper);
                    if (recommendation == null){
                        recommendation = new Recommendation();
                    }
                    recommendation.setLogId(userId);
                    recommendation.setType(type);
                    recommendation.setItemId(i);
                    recommendation.setScore(sortRecommend.get(type).get(i));
                    System.out.println(sortRecommend.get(type).get(i));
                    recommendationService.saveOrUpdate(recommendation);

                    // 超过10条推荐记录，则删除多余部分
                    QueryWrapper<Recommendation> wrapper1 = new QueryWrapper<>();
                    wrapper1.eq("log_id", userId);
                    wrapper1.eq("type", type);
                    int count = recommendationService.count(wrapper);
                    while (count >= 11){
                        wrapper1.orderByAsc("gmt_modified");
                        wrapper1.last("limit 1");
                        recommendationService.remove(wrapper1);
                        count -= 1;
                    }
                }
            }
        }
    }
}
