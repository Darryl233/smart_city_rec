package com.smartcity.service.kgservice.service;

import com.smartcity.service.kgservice.entity.MyEntity;
import com.smartcity.service.kgservice.tool.Neo4jProperties;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Entity;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

@Service
public class PaperService extends MyEntityService{

    private Driver driver;

    private Set<String> properties;

    public Set<Pair<String, String>> propertyValues;

    public PaperService() {
        this.driver = super.driver;
        this.properties = new HashSet<String>() {
            {
                this.add("title");
                this.add("authors");
                this.add("keywords");
                this.add("mechanicals");
                this.add("time");
                this.add("cited_count");
                this.add("download_count");
            }
        };
        super.properties = properties;
    }

    //根据id查询Entity,返回一个Entity对象或者Null.
    public MyEntity findPaperById(String paperID){
        return super.findEntityById(paperID);
    }

    public List<MyEntity> findPaperByTitle(String paperTitle){
        List<MyEntity> myEntities = new ArrayList<>();
        String cypher = "match (n:paper{title:$paperTitle}) return n";

        Map<String, Object> map = new HashMap<>();
        map.put("paperTitle", paperTitle);
        List<Record> records = runCypher(cypher, map);
        for (Record record : records) {
            Entity entity = record.get(0).asEntity();
            MyEntity myEntity = super.buildMyEntityWithEntity(entity);
            myEntities.add(myEntity);
        }
        if(myEntities.size() == 0){
            System.out.println("can't find a result in running PaperService.findPaperByTitle(" + paperTitle + ")");
            return null;
        }
        return myEntities;
    }
}
