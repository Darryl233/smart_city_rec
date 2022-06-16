package com.smartcity.service.rs.analyzer;

import java.util.*;

public class CollaborativeFilteringAnalyzer {
    /**
     * 计算用户之间的相似度
     * Calculate similarity value of users in lists and save value
     * into the recommendTable,value describes how popular the item is.
     * @param lists
     * @return recommendTable[][]
     */
    public HashMap<String, HashMap<String, Float>> userSimilarityConsine(HashMap<String, HashMap<Integer, List<String>>> lists) {
        Set<String> setsA = new HashSet<String>(), setsB = new HashSet<String>();
        HashMap<String, HashMap<String, Float>> recommendTable = new HashMap<String, HashMap<String, Float>>();
        HashMap<Integer, List<String>> a, b;
        List<Float> countList = new ArrayList<>();
        for(Map.Entry<String, HashMap<Integer, List<String>>> entry1: lists.entrySet())
        {
            //System.out.println("Key: "+ entry1.getKey()+ " Value: "+entry1.getValue());
            for(Map.Entry<String, HashMap<Integer, List<String>>> entry2: lists.entrySet())
            {
                //System.out.println("Key: "+ entry2.getKey()+ " Value: "+entry2.getValue());
                if (entry1.getKey() == entry2.getKey()){
                    continue;
                }
                for (int i=0; i<8; i++){
                    setsA.addAll(entry1.getValue().get(i+1));
                    setsB.addAll(entry2.getValue().get(i+1));
                    int sizeA = setsA.size(), sizeB = setsB.size();
                    setsA.retainAll(setsB);
                    if (sizeA * sizeB == 0){
                        countList.add((float) 0.0);
                    }
                    else{
                        countList.add((float) (setsA.size()/(Math.sqrt(sizeA * sizeB))));
                    }
                    setsA.clear();
                    setsB.clear();
                }
                float score = 0;
                for (float count : countList){
                    score += count;
                }
                countList.clear();
                if (!recommendTable.containsKey(entry1.getKey()))
                    recommendTable.put(entry1.getKey(), new HashMap<String, Float>());
                recommendTable.get(entry1.getKey()).put(entry2.getKey(), score);
                if (!recommendTable.containsKey(entry2.getKey()))
                    recommendTable.put(entry2.getKey(), new HashMap<String, Float>());
                recommendTable.get(entry2.getKey()).put(entry1.getKey(), score);
            }
        }
        return recommendTable;
    }

    /****
     * 根据用户之间的相似度，按类型获取所有相关item的推荐度《 类型Integet, <ItemsId, 推荐度> 》
     * @param lists: logid ： resourcetype： resourceidList
     * @param recommendTable: logid: logid: score,即用户和用户之间的相似度
     * @param userId: 用户的logId
     * @return
     */
    public HashMap<Integer, HashMap<String, Float>> getRecommendScore(HashMap<String, HashMap<Integer, List<String>>> lists,
                                                    HashMap<String, HashMap<String, Float>> recommendTable,
                                                    String userId){
        HashMap<Integer, HashMap<String, Float>> result = new HashMap<>();
        for (int i=1; i<9; i++){
            result.put(i, new HashMap<String, Float>());
        }
        if (recommendTable.get(userId) == null){
            return result;
        }
        List<Map.Entry<String, Float>> sortRecommendList = new ArrayList<Map.Entry<String, Float>>(recommendTable.get(userId).entrySet());
        //排序
        Collections.sort(sortRecommendList, new Comparator<Map.Entry<String, Float>>() {
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                if (o2.getValue() - o1.getValue() > 0)
                    return 1;
                else if (o2.getValue() - o1.getValue() == 0)
                    return 0;
                else
                    return -1;
            }
        });
        //相似度从高到低遍历，获取相似用户userB的id和相似度score
        for (int j=0; j< sortRecommendList.size(); j++){
            //System.out.println(sortRecommendList.get(j).toString());
            String userB_id = sortRecommendList.get(j).getKey();
            Float userB_score = sortRecommendList.get(j).getValue();
            //按8种资源类型进行遍历，按类型获取各类型下用户userB的所有浏览记录itemlist
            for (int i=1; i<9; i++){
                List<String> userB_itemlist = lists.get(userB_id).get(i);
                //遍历该类型下，用户userB的所有相关资源，累加计算该资源的推荐度
                for (int k=0; k< userB_itemlist.size(); k++){
                    String itemId = userB_itemlist.get(k);
                    //删除与用户userB重复的items
                    if (lists.get(userId).get(i).contains(itemId)){
                        continue;
                    }
                    Float score = result.get(i).get(itemId);
                    if (score == null) {
                        score = (float)0;
                    }
                    result.get(i).put(itemId, score+userB_score);
                }
            }
        }
        return result;
    }

    /***
     * 对推荐结果按照推荐度进行排序，每个类型保留range个
     * @param recommend: 类型Integer, <itemsId, 推荐度得分>
     * @param range:每个类型保留的个数
     * @return
     */
    public HashMap<Integer, HashMap<String, Float>> sortRecommend(HashMap<Integer, HashMap<String, Float>> recommend, int range){
        HashMap<Integer, HashMap<String, Float>> result = new HashMap<Integer, HashMap<String, Float>>();
        //按8种资源类型处理，按照推荐度得分从高到低进行排序
        for (int i=1; i<9; i++){
            result.put(i, new HashMap<String, Float>());
            List<Map.Entry<String, Float>> sortRecommendList = new ArrayList<Map.Entry<String, Float>>(recommend.get(i).entrySet());
            //排序
            Collections.sort(sortRecommendList, new Comparator<Map.Entry<String, Float>>() {
                public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                    if (o2.getValue() - o1.getValue() > 0)
                        return 1;
                    else if (o2.getValue() - o1.getValue() == 0)
                        return 0;
                    else
                        return -1;
                }
            });
            //排序完成后，获取前range或者sortRecommendList.size()个items 放进 result[i] 中
            for (int j=0; j<Math.min(range,sortRecommendList.size()); j++){
                result.get(i).put(sortRecommendList.get(j).getKey(), sortRecommendList.get(j).getValue());
            }
        }
        return result;
    }
}
