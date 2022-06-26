package com.smartcity.resources_crud.listener.cases;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.common.commonutils.BlockChainController;
import com.smartcity.common.commonutils.entity.BlockChainResource;
import com.smartcity.common.servicebase.exceptionhandler.CustomException;
import com.smartcity.resources_crud.entity.cases.SmartcityCase;
import com.smartcity.resources_crud.entity.cases.excel.CaseData;
import com.smartcity.resources_crud.service.ResourceService;


import java.text.SimpleDateFormat;

public class CaseExcelListener extends AnalysisEventListener<CaseData> {
    public ResourceService<SmartcityCase> caseService;

    public CaseExcelListener(){}
    public CaseExcelListener(ResourceService<SmartcityCase> service){
        this.caseService = service;
    }

    @Override
    public void invoke(CaseData caseData, AnalysisContext analysisContext) {
        if (caseData == null){
            throw new CustomException(20001, "caseData为空，文件数据为空");
        }
        SmartcityCase existCase = this.existCase(caseService, caseData.getKgId());
        if (existCase == null){
            SmartcityCase smartcityCase = new SmartcityCase();
            smartcityCase.setKgId(caseData.getKgId());
            smartcityCase.setUrl(caseData.getUrl());
            smartcityCase.setTitle(caseData.getTitle());
            smartcityCase.setRelationStaff(caseData.getRelationStaff());
            smartcityCase.setStaffInstitution(caseData.getStaffInstitution());
            smartcityCase.setStaffPhone(caseData.getStaffPhone());
            smartcityCase.setOrigin(caseData.getOrigin());
            smartcityCase.setIntroduction(caseData.getIntroduction());
            smartcityCase.setIndicator(caseData.getIndicator());
            smartcityCase.setApplication(caseData.getApplication());
            smartcityCase.setDomain(caseData.getDomain());
            caseService.save(smartcityCase);
            smartcityCase = this.existCase(caseService, caseData.getKgId());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            BlockChainResource resource = new BlockChainResource("0", "20", "null",
                    "Case_"+smartcityCase.getId(), formatter.format(smartcityCase.getGmtCreate()), "Admin_1");
            BlockChainController.setBlockChainResource(resource);
        }
    }

    //判断KG_id是否唯一，不能重复添加
    private SmartcityCase existCase(ResourceService<SmartcityCase> service, String id){
        QueryWrapper<SmartcityCase> wrapper = new QueryWrapper<>();
        wrapper.eq("kg_id", id);
        SmartcityCase smartcityCase = service.getOne(wrapper);
        return smartcityCase;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
