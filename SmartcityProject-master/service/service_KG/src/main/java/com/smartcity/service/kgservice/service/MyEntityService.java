package com.smartcity.service.kgservice.service;

import com.smartcity.service.kgservice.entity.MyEntity;
import com.smartcity.service.kgservice.tool.Neo4jProperties;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Entity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

@Service
public abstract class MyEntityService {

    @Resource
    Neo4jProperties neo4jProperties;

    protected Driver driver;

    protected Set<String> properties;

    public Map<String, String> propertyValues = new HashMap<>();

    @PostConstruct
    public void init(){
        driver = GraphDatabase.driver( neo4jProperties.getUri(),
                AuthTokens.basic( neo4jProperties.getUsername(), neo4jProperties.getPassword()) );

        properties = new HashSet<>();
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
            System.out.println("Cypher error occurs in running MyEntityService.runCypher(" + cypher + ")");
            return null;
        }
        return records;
    }

    MyEntity buildMyEntityWithEntity(Entity entity){
        String kg_id = entity.get("kg_id").asString();

        for (String property : properties) {
            if (!entity.get(property).isNull()) {
                String propertyValue = entity.get(property).asString();
                //Pair<String, String> pair = Pair.of(property, propertyValue);
                propertyValues.put(property, propertyValue);
            }
        }
        MyEntity myEntity = new MyEntity(kg_id, propertyValues);
        return myEntity;
    }

    //根据id查询Entity,返回一个Entity对象或者Null.
    public MyEntity findEntityById(String entityID){
        List<MyEntity> myEntities = new ArrayList<>();
        String cypher = "match (n{kg_id:$entityID}) return n";
        Map<String, Object> map = new HashMap<>();
        map.put("entityID", entityID);
        List<Record> records = runCypher(cypher, map);
        for (Record record : records) {
            Entity entity = record.get(0).asEntity();
            MyEntity myEntity = buildMyEntityWithEntity(entity);
            myEntities.add(myEntity);
        }
        if(myEntities.size() == 0){
            System.out.println("can't find a result in running MyEntityService.findEntityById(" + entityID + ")");
            return null;
        }
        return myEntities.get(0);
    }

    //根据id删除一个MyEntity
    public MyEntity deleteMyEntity(String entityID){
        List<MyEntity> myEntities = new ArrayList<>();
        String cypher = "match (n{kg_id:$entityID}) delete n return n";
        Map<String, Object> map = new HashMap<>();
        map.put("entityID", entityID);
        List<Record> records = runCypher(cypher, map);
        for (Record record : records) {
            Entity Entity = record.get(0).asEntity();
            MyEntity myEntity = buildMyEntityWithEntity(Entity);
            myEntities.add(myEntity);
        }
        if(myEntities.size() == 0){
            System.out.println("failed to delete a MyEntity in MyEntityService.deleteMyEntity, id:" + entityID);
            return null;
        }
        else {
            System.out.println("successful to delete a myMyEntity in entitieservice.deleteInstitution, id:" + entityID);
            return myEntities.get(0);
        }
    }

}
