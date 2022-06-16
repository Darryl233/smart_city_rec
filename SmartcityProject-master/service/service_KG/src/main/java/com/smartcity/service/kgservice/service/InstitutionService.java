package com.smartcity.service.kgservice.service;

import com.smartcity.service.kgservice.entity.KGInstitution;
import com.smartcity.service.kgservice.tool.Neo4jProperties;
import org.neo4j.driver.*;
import org.neo4j.driver.exceptions.ClientException;
import org.neo4j.driver.exceptions.Neo4jException;
import org.neo4j.driver.types.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

@Service
public class InstitutionService {
    @Resource
    Neo4jProperties neo4jProperties;

    private Driver driver;

    private Set<String> properties;

    @PostConstruct
    public void init(){
        driver = GraphDatabase.driver( neo4jProperties.getUri(),
                AuthTokens.basic( neo4jProperties.getUsername(), neo4jProperties.getPassword()) );

        properties = new HashSet<String>(){
            {
                this.add("unit_name");
                this.add("por1");
                this.add("por2");
            }
        };
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
        catch (Neo4jException neo4jException){
            System.out.println("Cypher error occurs in running InstitutionService.runCypher(" + cypher + ")");
            return null;
        }
        return records;
    }

    KGInstitution buildInstitutionWithEntity(Entity entity){
        String id = entity.get("kg_id").asString();
        String name = entity.get("unit_name").asString();
        KGInstitution institution = new KGInstitution(id, name);
        return institution;
    }

    //根据名称或id查询institution，两者都具有唯一性
    public KGInstitution findInstitutionByNameOrId(String institutionNameOrId){
        List<KGInstitution> institutions = new ArrayList<>();
        String cypher = "";
        if(institutionNameOrId.startsWith("unit")){
            cypher = "match (n:unit{kg_id:$institutionNameOrId}) return n";
        }
        else{
            cypher = "match (n:unit{unit_name:$institutionNameOrId}) return n";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("institutionNameOrId", institutionNameOrId);
        List<Record> records = runCypher(cypher, map);
        for (Record record : records) {
            Entity entity = record.get(0).asEntity();
            KGInstitution institution = buildInstitutionWithEntity(entity);
            institutions.add(institution);
        }
        if(institutions.size() == 0){
            System.out.println("can't find a result in running InstitutionService.findInstitutionByNameOrId(" + institutionNameOrId + ")");
            return null;
        }
        return institutions.get(0);
    }

    //模糊查询，查询名称中包含name的institution
    public List<KGInstitution> findInstitutionsByFuzzyName(String name){
        List<KGInstitution> institutions = new ArrayList<>();
        String cypher = "match (n:unit) where n.unit_name =~ '.*" + name + ".*' return n";
        //System.out.println("cypher = " + cypher);
        List<Record> records = runCypher(cypher, null);
        for (Record record : records) {
            Entity entity = record.get(0).asEntity();
            KGInstitution institution = buildInstitutionWithEntity(entity);
            institutions.add(institution);
        }
        if(institutions.size() == 0){
            System.out.println("can't find a result in running InstitutionService.findInstitutionByFuzzyNameOrId(" + name + ")");
            return null;
        }
        return institutions;
    }

    //根据专家的id找到其对应的institution
    public KGInstitution findInstitutionByExpertId(String expertId){
        List<KGInstitution> institutions = new ArrayList<>();
        String cypher = "match (:expert{kg_id:$expertId})-[:就职]->(n) return n";
        Map<String, Object> map = new HashMap<>();
        map.put("expertId", expertId);
        List<Record> records = runCypher(cypher, map);
        for (Record record : records) {
            Entity entity = record.get(0).asEntity();
            KGInstitution institution = buildInstitutionWithEntity(entity);
            institutions.add(institution);
        }
        if(institutions.size() == 0){
            System.out.println("can't find a result in running InstitutionService.findInstitutionByExpertId(" + expertId + ")");
            return null;
        }
        return institutions.get(0);
    }

    //根据id删除一个institution
    public KGInstitution deleteInstitution(String institutionId){
        List<KGInstitution> institutions = new ArrayList<>();
        String cypher = "match (n{kg_id:$institutionId}) delete n return n";
        Map<String, Object> map = new HashMap<>();
        map.put("institutionId", institutionId);
        List<Record> records = runCypher(cypher, map);
        //System.out.println("records = " + records);
        for (Record record : records) {
            Entity entity = record.get(0).asEntity();
            KGInstitution institution = buildInstitutionWithEntity(entity);
            institutions.add(institution);
        }
        if(institutions.size() == 0){
            System.out.println("failed to delete a institution in InstitutionService.deleteInstitution, id:" + institutionId);
            return null;
        }
        else {
            System.out.println("successful to delete a institution in InstitutionService.deleteInstitution, id:" + institutionId);
            return institutions.get(0);
        }
    }

    //根据id修改属性值
    public KGInstitution updateInstitution(String institutionId, Map<String, Object> propertyAndValues){
        Set<String> rowKey = propertyAndValues.keySet();
        if(!properties.containsAll(rowKey)){
            System.out.println("illegal property exists in your map, your properties is:" + rowKey);
            return null;
        }

        List<KGInstitution> institutions = new ArrayList<>();
        String cypher = "match (n{kg_id:$institutionId}) set ";
        for (String key : propertyAndValues.keySet()) {
            String subCypher = "n." + key + "='" + propertyAndValues.get(key) + "', ";
            cypher = cypher.concat(subCypher);
        }
        cypher = cypher.substring(0, cypher.length()-2);
        cypher = cypher.concat(" return n");
        System.out.println("cypher = " + cypher);
        Map<String, Object> map = new HashMap<>();
        map.put("institutionId", institutionId);
        List<Record> records = runCypher(cypher, map);
        for (Record record : records) {
            Entity entity = record.get(0).asEntity();
            KGInstitution institution = buildInstitutionWithEntity(entity);
            institutions.add(institution);
        }
        if(institutions.size() == 0){
            System.out.println("failed to update a institution in InstitutionService.updateInstitution, id:" + institutionId);
            return null;
        }
        else {
            System.out.println("successful to update a institution in InstitutionService.updateInstitution, id:" + institutionId);
            return institutions.get(0);
        }
    }

    //增加一个institution节点，先判断id是否已存在，如不存在，再插入
    public KGInstitution insertInstitution(String institutionId, Map<String, Object> propertyAndValues){
        String existCypher = "match (n{kg_id:$institutionId}) return n";
        Map<String, Object> map = new HashMap<>();
        map.put("institutionId", institutionId);
        List<Record> recordsEx = runCypher(existCypher, map);
        if(recordsEx.size() != 0){
            System.out.println("same institutionId has exists in neo4j, couldn't insert the institution with id:" + institutionId);
            return null;
        }

        Set<String> rowKey = propertyAndValues.keySet();
        if(!properties.containsAll(rowKey)){
            System.out.println("illegal property exists in your map, your properties is:" + rowKey);
            return null;
        }

        List<KGInstitution> institutions = new ArrayList<>();
        String insertCypher = "create (n:unit{kg_id:$institutionId}) set ";
        for (String key : propertyAndValues.keySet()) {
            String subCypher = "n." + key + "='" + propertyAndValues.get(key) + "', ";
            insertCypher = insertCypher.concat(subCypher);
        }
        insertCypher = insertCypher.substring(0, insertCypher.length()-2);
        insertCypher = insertCypher.concat(" return n");
        //System.out.println("cypher = " + insertCypher);

        List<Record> records = runCypher(insertCypher, map);
        for (Record record : records) {
            Entity entity = record.get(0).asEntity();
            KGInstitution institution = buildInstitutionWithEntity(entity);
            institutions.add(institution);
        }
        if(institutions.size() == 0){
            System.out.println("failed to insert a institution in InstitutionService.insertInstitution, id:" + institutionId);
            return null;
        }
        else{
            System.out.println("successful to insert a institution in InstitutionService.insertInstitution, id:" + institutionId);
            return institutions.get(0);
        }
    }

    public List<String []> extractSubGraph(String institutionId){

        List<String []> tuples = new ArrayList<>();
        String cypher1 = "match (root:unit{unit_id:$institutionId})-[rels*1..3]-()\n" +
                "unwind rels as rel\n" +
                "return distinct \n" +
                "case labels(startNode(rel))[0]\n" +
                "\twhen 'unit' then startNode(rel)['unit_id']\n" +
                "    when 'expert' then startNode(rel)['expert_id']\n" +
                "    when 'requirement' then startNode(rel)['requirement_id']\n" +
                "end as head_id,\n" +
                "case labels(startNode(rel))[0]\n" +
                "\twhen 'unit' then startNode(rel)['unit_name']\n" +
                "    when 'expert' then startNode(rel)['expert_name']\n" +
                "    when 'requirement' then startNode(rel)['requirement_name']\n" +
                "end as head_name,\n" +
                "type(rel),\n" +
                "case labels(endNode(rel))[0]\n" +
                "\twhen 'case' then endNode(rel)[\"case_id\"]\n" +
                "    when 'requirement' then endNode(rel)[\"requirement_id\"]\n" +
                "    when 'solution' then endNode(rel)['solution_id']\n" +
                "    when \"paper\" then endNode(rel)[\"paper_id\"]\n" +
                "    when \"patent\" then endNode(rel)[\"patent_id\"]\n" +
                "    when \"achievement\" then endNode(rel)[\"achievement_id\"]\n" +
                "    when \"unit\" then endNode(rel)[\"unit_id\"]\n" +
                "end as tail_id,\n" +
                "case labels(endNode(rel))[0]\n" +
                "\twhen 'case' then endNode(rel)[\"title\"]\n" +
                "    when 'requirement' then endNode(rel)[\"requirement_name\"]\n" +
                "    when 'solution' then endNode(rel)['solution_name']\n" +
                "    when \"paper\" then endNode(rel)[\"title\"]\n" +
                "    when \"patent\" then endNode(rel)[\"title\"]\n" +
                "    when \"achievement\" then endNode(rel)[\"title\"]\n" +
                "    when \"unit\" then endNode(rel)[\"unit_name\"]\n" +
                "end as tail_name\n" +
                "order by(head_id)\n";

        try( Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                Result result = tx.run(cypher1, Values.parameters("institutionId", institutionId));
                while(result != null && result.hasNext()){
                    Record record = result.next();
                    //System.out.println(record.get(0).asString() + "," + record.get(1).asString() + "," + record.get(2).asString());
                    String[] tuple = new String[5];
                    tuple[0] = record.get(0).asString();    //head_id
                    tuple[1] = record.get(1).asString();    //head_name
                    tuple[2] = record.get(2).asString();    //relation_type
                    tuple[3] = record.get(3).asString();    //tail_id
                    tuple[4] = record.get(4).asString();    //tail_name
                    tuples.add(tuple);
                }
                tx.commit();
            }
        }
        catch (ClientException clientException){
            System.out.println("Cypher error occurs in running InstitutionService.extractSubGraph(" + institutionId + ")");
            return null;
        }
        return tuples;
    }
}
