package com.smartcity.service.kgservice.service;

import com.smartcity.service.kgservice.tool.Neo4jProperties;
import com.smartcity.service.kgservice.tool.Tool;
import org.neo4j.driver.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
public class RelationService {

    @Resource
    Neo4jProperties neo4jProperties;

    private Driver driver;

    @PostConstruct
    public void init(){
        driver = GraphDatabase.driver( neo4jProperties.getUri(),
                AuthTokens.basic( neo4jProperties.getUsername(), neo4jProperties.getPassword()) );
    }

    //根据时间戳生成21位的关系id，精确到1毫秒。如果有大批量关系同时生成，请注意插入时间间隔
    public String createRelationID(String headID, String tailID){
        int headType = Tool.findEntityTypeByID(headID);
        int tailType = Tool.findEntityTypeByID(tailID);
        String strTime = Tool.createTime();

        String newID = String.valueOf(headType) + "&" + String.valueOf(tailType) + "_" + strTime;
        return newID;
    }

    List<Record> runCypher(String cypher, Map<String, Object> params){
        List<Record> records = null;
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                if(params == null){
                    records = tx.run(cypher).list();
                }
                else{
                    records = tx.run(cypher, params).list();
                }
                tx.commit();
            }
        }
        catch (RuntimeException runtimeException){
            System.out.println("Error information by system:" + runtimeException.getMessage());
            System.out.println("Cypher error occurs in running ExpertService.runCypher(" + cypher + ")");
            return null;
        }
        return records;
    }

    //给出头节点与尾节点id，在两者之间建立relationType关系。
    //由于两个实体之间可以有多种类型的关系，所以先判断是否已有该类关系，没有再创建
    public String insertRelation(String headID, String tailID, String relationType) {
        ////根据输入的头、尾实体类型，判断输入的关系类型是否合法
        int headType = Tool.findEntityTypeByID(headID);
        int tailType = Tool.findEntityTypeByID(tailID);
        if(!Tool.legalRelationType(headType, tailType, relationType)){
            System.out.println("insert failed, illegal relation type with headID and tailID, :(" + headID + ", " + relationType + ", " + tailID + ")");
            return null;
        }

        //判断这两个实体之间是否已经有该类型的关系，如果没有再插入
        String existCypher = "match ({kg_id:$headID})-[rels]->({kg_id:$tailID})\n" +
                "with collect(type(rels)) as relTypes\n" +
                "WHERE any(x IN relTypes WHERE x=$relationType)\n" +
                "RETURN relTypes";
        Map<String, Object> map = new HashMap<>();
        map.put("headID", headID);
        map.put("tailID", tailID);
        map.put("relationType", relationType);

        List<Record> existRecords = runCypher(existCypher, map);
        if(existRecords.size() > 0){
            System.out.println("insert failed, exists a same type relation in neo4j with headID, tailID and type are:(" + headID + ", " + tailID + ", " + relationType + ")" );
            return null;
        }

        String relationId = createRelationID(headID, tailID);
        String insertCypher = "match (head{kg_id:$headID}), (tail{kg_id:$tailID})  \n" +
                "create (head)-[rel:" + relationType + "{id:$relationId}]->(tail)\n" +
                "return rel";

        String getID = "";
        //System.out.println("insertCypher = " + insertCypher);

        map.remove("relationType");
        map.put("relationId", relationId);

        List<Record> records = runCypher(insertCypher, map);
        for (Record record : records) {
            getID = record.get(0).asMap().get("id").toString();
        }
        if(getID == null || getID.equals("")){
            System.out.println("failed to insert a relation in RelationService.insertRelation, headID, tailID and type are:(" + headID + ", " + tailID + ", " + relationType + ")" );
            return "";
        }
        else{
            System.out.println("successful to insert a relation in RelationService.insertRelation, headID, tailID and type are:(" + headID + ", " + tailID + ", " + relationType + ")" );
            return getID;
        }
    }

    //根据关系的id删除一条关系
    public int deleteRelationByID(String relationID){
        String Cypher = "match ()-[rel{id:$relationID}]->() delete rel return rel";

        int deleteType = -1;

        Map<String, Object> map = new HashMap<>();
        map.put("relationID", relationID);
        List<Record> records = runCypher(Cypher, map);
        if(records.size() > 0){
            deleteType = 1;
        }
        if(deleteType == -1){
            System.out.println("failed to delete a relation in RelationService.deleteRelationByID, relationID is:(" + relationID + ")" );
        }
        else{
            System.out.println("successful to delete a relation in RelationService.deleteRelationByID, relationID is:(" + relationID + ")" );
        }
        return deleteType;
    }

    //根据头ID和尾ID,删除两者间的relationType关系(H:head, R:relationType, T:tail)
    public int deleteRelationByHRT(String headID, String tailID, String relationType){
        String Cypher = "match ({kg_id:$headID})-[delRel:" + relationType +"]->({kg_id:$tailID}) delete delRel return delRel";

        int deleteType = -1;

        Map<String, Object> map = new HashMap<>();
        map.put("headID", headID);
        map.put("tailID", tailID);
        List<Record> records = runCypher(Cypher, map);
        if(records.size() > 0){
            deleteType = 1;
        }
        if(deleteType == -1){
            System.out.println("failed to delete a relation in RelationService.deleteRelationByID, headID, tailID and type are:(" + headID + ", " + tailID + ", " + relationType + ")" );
        }
        else{
            System.out.println("successful to delete a relation in RelationService.deleteRelationByID, headID, tailID and type are:(" + headID + ", " + tailID + ", " + relationType + ")" );
        }
        return deleteType;
    }

    //查询一个实体都与哪些实体有关系，返回实体id和对应关系类型
    public Map<String, String> findRelationsByEntityID(String entityID){
        String Cypher = "match (n{kg_id:$entityID})-[rels]-(entities)\n" +
                "return distinct type(rels), entities['kg_id']";

        Map<String, String> relateER = new HashMap<>();
        //System.out.println("insertCypher = " + insertCypher);

        Map<String, Object> map = new HashMap<>();
        map.put("entityID", entityID);
        List<Record> records = runCypher(Cypher, map);
        for (Record record : records) {
            String relationType = record.get(0).asString();
            String relatedEntityID = record.get(1).asString();
            relateER.put(relatedEntityID, relationType);
        }
        return relateER;
    }
}
