package com.smartcity.service.relationservice.listen;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.common.servicebase.exceptionhandler.CustomException;
import com.smartcity.service.relationservice.entity.Relations;
import com.smartcity.service.relationservice.entity.excel.RelationData;
import com.smartcity.service.relationservice.service.RelationsService;
import lombok.SneakyThrows;


public class RelationExcelListener extends AnalysisEventListener<RelationData> {

    public RelationsService relationsService;

    public RelationExcelListener(){}
    public RelationExcelListener(RelationsService service){
        this.relationsService = service;
    }

    @SneakyThrows
    @Override
    public void invoke(RelationData relationData, AnalysisContext analysisContext) {
        if (relationData == null){
            throw new CustomException(20001, "relationData为空");
        }
        Relations existRelation = this.existRelation(this.relationsService, relationData.getRelationId());
        if (existRelation == null){
            Relations relations = new Relations();
            relations.setRelationId(relationData.getRelationId());
            relations.setHeadId(relationData.getHeadId());
            relations.setTailId(relationData.getTailId());
            relationsService.save(relations);
        }
    }

    private Relations existRelation(RelationsService service, String id){
        QueryWrapper<Relations> wrapper = new QueryWrapper<>();
        wrapper.eq("relation_id", id);
        Relations relations = service.getOne(wrapper);
        return relations;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
