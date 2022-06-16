package com.smartcity.service.kgservice.tool;

import org.joda.time.DateTime;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Tool {
    static private final Map<String, Integer> entityTypeMap = new HashMap<String, Integer>(){{
        put("expert", 1);
        put("unit", 2);
        put("requirement", 3);
        put("solution", 4);
        put("case", 5);
        put("achievement", 6);
        put("paper", 7);
        put("patent", 8);
        put("software", 9);
    }};

    static private final List<Pair<Pair<Integer, Integer>, String>> relationTypeList = new ArrayList<Pair<Pair<Integer, Integer>, String>>(){{
        add(Pair.of(Pair.of(1, 2), "就职"));
        //add(Pair.of(Pair.of(1, 2), "管理"));
        add(Pair.of(Pair.of(1, 4), "提出"));
        add(Pair.of(Pair.of(1, 5), "提出"));
        add(Pair.of(Pair.of(1, 6), "提出"));
        add(Pair.of(Pair.of(1, 7), "提出"));
        add(Pair.of(Pair.of(1, 8), "提出"));
        add(Pair.of(Pair.of(1, 9), "提出"));
        add(Pair.of(Pair.of(1, 3), "上传"));
        add(Pair.of(Pair.of(1, 4), "上传"));
        add(Pair.of(Pair.of(1, 5), "上传"));
        add(Pair.of(Pair.of(1, 6), "上传"));
        add(Pair.of(Pair.of(1, 7), "上传"));
        add(Pair.of(Pair.of(1, 8), "上传"));
        add(Pair.of(Pair.of(1, 9), "上传"));
        add(Pair.of(Pair.of(1, 3), "下载"));
        add(Pair.of(Pair.of(1, 4), "下载"));
        add(Pair.of(Pair.of(1, 5), "下载"));
        add(Pair.of(Pair.of(1, 6), "下载"));
        add(Pair.of(Pair.of(1, 7), "下载"));
        add(Pair.of(Pair.of(1, 8), "下载"));
        add(Pair.of(Pair.of(1, 9), "下载"));

        add(Pair.of(Pair.of(2, 3), "提出"));
        add(Pair.of(Pair.of(2, 4), "提出"));
        add(Pair.of(Pair.of(2, 5), "提出"));

        add(Pair.of(Pair.of(3, 4), "使用"));
        add(Pair.of(Pair.of(5, 6), "使用"));
        add(Pair.of(Pair.of(6, 7), "使用"));
        add(Pair.of(Pair.of(6, 8), "使用"));
    }};


    //根据当前时间生成17位字符串，精确到1毫秒。如果有大批量id同时生成，请注意插入时间间隔
    public static String createTime(){
        DateTime time= new DateTime();
        String strTime = time.toString();
        StringBuilder result = new StringBuilder(strTime);
        result.delete(result.length()-6, result.length());

        int[] deleteIndex = {19, 16, 13, 10, 7, 4};
        //int[] deleteIndex = {4, 7, 10, 13, 16, 19};
        for(int index :deleteIndex){
            result.delete(index, index+1);
        }
        return result.toString();
    }

    public static int findEntityTypeByID(String entityID){
        String typeStr = entityID.substring(0,4);
        for (Map.Entry<String, Integer> entry : entityTypeMap.entrySet()) {
            String nowKey = entry.getKey();
            if(nowKey.startsWith(typeStr))
                return entry.getValue();
        }
        return -1;
    }



    //根据输入的头、尾实体类型，判断输入的关系类型是否合法
    public static boolean legalRelationType(int headType, int tailType, String relationType){
        Pair<Integer, Integer> nodeType = Pair.of(headType, tailType);
        for(Pair<Pair<Integer, Integer>, String> nowType : relationTypeList){
            if(nodeType.equals(nowType.getFirst()) && relationType.equals(nowType.getSecond())){
                return true;
            }
        }
        return false;
    }
}


