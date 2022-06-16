package com.smartcity.service.kgservice.service;

import com.smartcity.service.kgservice.entity.KGExpert;
import com.smartcity.service.kgservice.tool.Neo4jProperties;
import org.neo4j.driver.*;
import org.neo4j.driver.exceptions.ClientException;
import org.neo4j.driver.types.Entity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

@Service
public class ExpertService {
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
                this.add("expert_name");
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
        catch (RuntimeException runtimeException){
            System.out.println("Error information by system:" + runtimeException.getMessage());
            System.out.println("Cypher error occurs in running ExpertService.runCypher(" + cypher + ")");
            return null;
        }
        return records;
    }

    KGExpert buildExpertWithEntity(Entity entity){
        String id = entity.get("kg_id").asString();
        String name = entity.get("expert_name").asString();
        KGExpert expert = new KGExpert(id, name);
        return expert;
    }

    //根据id查询Expert,返回一个Expert对象或者Null.
    public KGExpert findExpertById(String expertId){
        List<KGExpert> experts = new ArrayList<>();
        String cypher = "match (n{kg_id:$expertId}) return n";
        Map<String, Object> map = new HashMap<>();
        map.put("expertId", expertId);
        List<Record> records = runCypher(cypher, map);
        for (Record record : records) {
            Entity entity = record.get(0).asEntity();
            KGExpert expert = buildExpertWithEntity(entity);
            experts.add(expert);
        }
        if(experts.size() == 0){
            System.out.println("can't find a result in running ExpertService.findExpertById(" + expertId + ")");
            return null;
        }
        return experts.get(0);
    }

    //根据名称查询Expert,返回一个List<Expert>对象或者Null.
    public List<KGExpert> findExpertByName(String expertName){
        List<KGExpert> experts = new ArrayList<>();
        String cypher = "match (n{expert_name:$expertName}) return n";

        Map<String, Object> map = new HashMap<>();
        map.put("expertName", expertName);
        List<Record> records = runCypher(cypher, map);
        for (Record record : records) {
            Entity entity = record.get(0).asEntity();
            KGExpert expert = buildExpertWithEntity(entity);
            experts.add(expert);
        }
        if(experts.size() == 0){
            System.out.println("can't find a result in running ExpertService.findExpertByName(" + expertName + ")");
            return null;
        }
        return experts;
    }

    //模糊查询，查询名称中包含name的expert
    public List<KGExpert> findExpertsByFuzzyName(String name){
        List<KGExpert> experts = new ArrayList<>();
        String cypher = "match (n:expert) where n.expert_name =~ '.*" + name + ".*' return n";
        List<Record> records = runCypher(cypher, null);
        for (Record record : records) {
            Entity entity = record.get(0).asEntity();
            KGExpert expert = buildExpertWithEntity(entity);
            experts.add(expert);
        }
        if(experts.size() == 0){
            System.out.println("can't find a result in running ExpertService.findExpertsByFuzzyName(" + name + ")");
            return null;
        }
        return experts;
    }

    //根据institution的id找到其对应的所有expert
    public List<KGExpert> findExpertsByInstitutionId(String institutionId){
        List<KGExpert> experts = new ArrayList<>();
        String cypher = "match (n:expert)-[:`就职`]->(:unit{kg_id:$institutionId}) return n";
        Map<String, Object> map = new HashMap<>();
        map.put("institutionId", institutionId);
        List<Record> records = runCypher(cypher, map);
        for (Record record : records) {
            Entity entity = record.get(0).asEntity();
            KGExpert expert = buildExpertWithEntity(entity);
            experts.add(expert);
        }
        if(experts.size() == 0){
            System.out.println("can't find a result in running ExpertService.findExpertsByInstitutionId(" + institutionId + ")");
            return null;
        }
        return experts;
    }

    //根据institution的name找到其对应的所有expert
    public List<KGExpert> findExpertsByInstitutionName(String institutionName){
        List<KGExpert> experts = new ArrayList<>();
        String cypher = "match (n:expert)-[:`就职`]->(:unit{unit_name:$institutionName}) return n";
        Map<String, Object> map = new HashMap<>();
        map.put("institutionName", institutionName);
        List<Record> records = runCypher(cypher, map);
        for (Record record : records) {
            Entity entity = record.get(0).asEntity();
            KGExpert expert = buildExpertWithEntity(entity);
            experts.add(expert);
        }
        if(experts.size() == 0){
            System.out.println("can't find a result in running ExpertService.findExpertsByInstitutionName(" + institutionName + ")");
            return null;
        }
        return experts;
    }

    //根据id删除一个expert
    public KGExpert deleteExpert(String expertId){
        List<KGExpert> experts = new ArrayList<>();
        String cypher = "match (n{kg_id:$expertId}) delete n return n";
        Map<String, Object> map = new HashMap<>();
        map.put("expertId", expertId);
        List<Record> records = runCypher(cypher, map);
        for (Record record : records) {
            Entity entity = record.get(0).asEntity();
            KGExpert expert = buildExpertWithEntity(entity);
            experts.add(expert);
        }
        if(experts.size() == 0){
            System.out.println("failed to delete a expert in ExpertService.deleteExpert, id:" + expertId);
            return null;
        }
        else {
            System.out.println("successful to delete a expert in ExpertService.deleteInstitution, id:" + expertId);
            return experts.get(0);
        }
    }

    //根据id修改属性值
    public KGExpert updateExpert(String expertId, Map<String, Object> propertyAndValues){
        Set<String> rowKey = propertyAndValues.keySet();
        if(!properties.containsAll(rowKey)){
            System.out.println("illegal property exists in your map, your properties is:" + rowKey);
            return null;
        }

        List<KGExpert> experts = new ArrayList<>();
        String cypher = "match (n:expert{kg_id:$expertId}) set ";
        for (String key : propertyAndValues.keySet()) {
            String subCypher = "n." + key + "='" + propertyAndValues.get(key) + "', ";
            cypher = cypher.concat(subCypher);
        }
        cypher = cypher.substring(0, cypher.length()-2);
        cypher = cypher.concat(" return n");
        System.out.println("cypher = " + cypher);
        Map<String, Object> map = new HashMap<>();
        map.put("expertId", expertId);
        List<Record> records = runCypher(cypher, map);
        for (Record record : records) {
            Entity entity = record.get(0).asEntity();
            System.out.println("entity = " + entity);
            KGExpert expert = buildExpertWithEntity(entity);
            experts.add(expert);
        }
        if(experts.size() == 0){
            System.out.println("failed to update a expert in ExpertService.updateExpert, id:" + expertId);
            return null;
        }
        else {
            System.out.println("successful to update a expert in ExpertService.updateExpert, id:" + expertId);
            return experts.get(0);
        }
    }

    //增加一个expert节点，先判断id是否已存在，如不存在，再插入
    public KGExpert insertExpert(String expertId, Map<String, Object> propertyAndValues){
        String existCypher = "match (n:expert{kg_id:$expertId}) return n";
        Map<String, Object> map = new HashMap<>();
        map.put("expertId", expertId);
        List<Record> recordsEx = runCypher(existCypher, map);
        if(recordsEx.size() != 0){
            System.out.println("same expertId has exists in neo4j, couldn't insert the expert with id:" + expertId);
            return null;
        }

        Set<String> rowKey = propertyAndValues.keySet();
        if(!properties.containsAll(rowKey)){
            System.out.println("illegal property exists in your map, your properties is:" + rowKey);
            return null;
        }

        List<KGExpert> experts = new ArrayList<>();
        String insertCypher = "create (n:expert{kg_id:$expertId}) set ";
        for (String key : propertyAndValues.keySet()) {
            String subCypher = "n." + key + "='" + propertyAndValues.get(key) + "', ";
            insertCypher = insertCypher.concat(subCypher);
        }
        insertCypher = insertCypher.substring(0, insertCypher.length()-2);
        insertCypher = insertCypher.concat(" return n");
        System.out.println("cypher = " + insertCypher);

        List<Record> records = runCypher(insertCypher, map);
        for (Record record : records) {
            Entity entity = record.get(0).asEntity();
            KGExpert expert = buildExpertWithEntity(entity);
            experts.add(expert);
        }
        if(experts.size() == 0){
            System.out.println("failed to insert a expert in ExpertService.insertExpert, id:" + expertId);
            return null;
        }
        else{
            System.out.println("successful to insert a expert in ExpertService.insertExpert, id:" + expertId);
            return experts.get(0);
        }
    }

    //返回根节点3层之内的子图
    public List<String []> extractSubGraph(String expertId){
        List<String []> tuples = new ArrayList<>();
        String cypher = "match (root:expert{kg_id:$expertId})-[rels*1..3]-()\n" +
                "unwind rels as rel\n" +
                "return distinct startNode(rel)['kg_id'] as head_id,\n" +
                "case labels(startNode(rel))[0]\n" +
                "\twhen 'unit' then startNode(rel)['unit_name']\n" +
                "    when 'expert' then startNode(rel)['expert_name']\n" +
                "    when 'requirement' then startNode(rel)['requirement_name']\n" +
                "end as head_name,\n" +
                "type(rel),\n" +
                "endNode(rel)[\"kg_id\"] as tail_id, \n" +
                "case labels(endNode(rel))[0]\n" +
                "\twhen 'case' then endNode(rel)[\"title\"]\n" +
                "    when 'requirement' then endNode(rel)[\"requirement_name\"]\n" +
                "    when 'solution' then endNode(rel)['solution_name']\n" +
                "    when \"paper\" then endNode(rel)[\"title\"]\n" +
                "    when \"patent\" then endNode(rel)[\"title\"]\n" +
                "    when \"achievement\" then endNode(rel)[\"title\"]\n" +
                "    when \"unit\" then endNode(rel)[\"unit_name\"]\n" +
                "end as tail_name\n" +
                "order by(head_id)";

        try(Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                Result result = tx.run(cypher, Values.parameters("expertId", expertId));
                while(result.hasNext()){
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
            System.out.println("Cypher error occurs in running ExpertService.extractSubGraph(" + expertId + ")");
            return null;
        }
        return tuples;
    }

    //返回根节点3层之内的子图
    public List<String []> extractSubGraphOOD(String expertId){
        List<String []> tuples = new ArrayList<>();
        String cypher = "match (root:expert{expert_id:$expertId})-[rel1:`提出`]-(root_source)<-[rel2:`提出`]-(other_author:expert)\n" +
                "with root, other_author, rel1, rel2, root_source\n" +
                "match (other_author)-[rel3:`提出`]->(other_source)\n" +
                "match (root)-[rel4:`就职`]->(root_unit)\n" +
                "match (other_author)-[rel5:`就职`]->(other_unit)\n" +
                "with rel1, rel2, rel3, rel4, rel5,collect(rel1)+collect(rel2)+collect(rel3)+collect(rel4)+collect(rel5) as rels\n" +
                "unwind rels as rel\n" +
                "return distinct startNode(rel).expert_id, rel.id, \n" +
                "case labels(endNode(rel))[0]\n" +
                "\twhen \"paper\" then endNode(rel)[\"paper_id\"]\n" +
                "    when \"patent\" then endNode(rel)[\"patent_id\"]\n" +
                "    when \"achievement\" then endNode(rel)[\"achievement_id\"]\n" +
                "    when \"unit\" then endNode(rel)[\"unit_id\"]\n" +
                "    else \"none\"\n" +
                "end\n" +
                "order by startNode(rel).expert_id";

        try(Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                Result result = tx.run(cypher, Values.parameters("expertId", expertId));
                while(result.hasNext()){
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
            System.out.println("Cypher error occurs in running ExpertService.extractSubGraph(" + expertId + ")");
            return null;
        }
        return tuples;
    }
}

