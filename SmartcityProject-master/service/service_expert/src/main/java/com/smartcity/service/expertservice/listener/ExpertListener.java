package com.smartcity.service.expertservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.common.commonutils.BlockChainController;
import com.smartcity.common.commonutils.entity.BlockChainResource;
import com.smartcity.common.servicebase.exceptionhandler.CustomException;
import com.smartcity.service.expertservice.entity.SmartcityExpert;
import com.smartcity.service.expertservice.entity.excel.ExpertData;
import com.smartcity.service.expertservice.service.SmartcityExpertService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpertListener extends AnalysisEventListener<ExpertData> {

    public SmartcityExpertService expertService;

    public ExpertListener(){}
    public ExpertListener(SmartcityExpertService service){
        this.expertService = service;
    }

    @Override
    public void invoke(ExpertData expertData, AnalysisContext analysisContext) {
        if (expertData == null){
            throw new CustomException(20001, "ExpertData为空，文件数据为空");
        }
        SmartcityExpert existExpert = this.existExpert(expertService, expertData.getKgId());
        if (existExpert == null){
            SmartcityExpert expert = new SmartcityExpert();
            expert.setKgId(expertData.getKgId());
            expert.setName(expertData.getName());
            expert.setIntro(expertData.getIntro());
            expert.setCareer(expertData.getCareer());
            expert.setEmail(expertData.getEmail());
            expert.setPhone(expertData.getPhone());
            expert.setFax(expertData.getFax());
            expert.setInstitution(expertData.getInstitution());
            expert.setCity(expertData.getCity());
            expert.setDomain(expertData.getDomain());
            expertService.save(expert);
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    //判断KG_id是否唯一，不能重复添加
    private SmartcityExpert existExpert(SmartcityExpertService service, String kgId){
        QueryWrapper<SmartcityExpert> wrapper = new QueryWrapper<>();
        wrapper.eq("kg_id", kgId);
        SmartcityExpert expert = service.getOne(wrapper);
        return expert;
    }
}
