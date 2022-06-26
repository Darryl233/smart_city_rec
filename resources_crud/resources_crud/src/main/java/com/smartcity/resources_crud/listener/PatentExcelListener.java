package com.smartcity.resources_crud.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.common.commonutils.BlockChainController;
import com.smartcity.common.commonutils.entity.BlockChainResource;
import com.smartcity.common.servicebase.exceptionhandler.CustomException;
import com.smartcity.resources_crud.entity.patent.SmartcityPatent;
import com.smartcity.resources_crud.entity.patent.excel.PatentData;
import com.smartcity.resources_crud.service.ResourceService;
import com.smartcity.resources_crud.service.patent.impl.SmartcityPatentServiceImpl;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;

public class PatentExcelListener extends AnalysisEventListener<PatentData> {

    public ResourceService<SmartcityPatent> patentService;

    public PatentExcelListener(){}
    public PatentExcelListener(ResourceService<SmartcityPatent> patentService){
        this.patentService = patentService;
    }

    @Override
    @SneakyThrows
    public void invoke(PatentData patentData, AnalysisContext analysisContext) {
        if(patentData == null){
            throw new CustomException(20001, "patentData为空，文件数据为空");
        }
        SmartcityPatent existPatent = this.existPatent(patentService, patentData.getKgId());
        if(existPatent == null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SmartcityPatent patent = new SmartcityPatent();
            patent.setKgId(patentData.getKgId());
            patent.setUrl(patentData.getUrl());
            patent.setTitle(patentData.getTitle());
            if (!StringUtils.isEmpty(patentData.getApplicationDate())){
                patent.setApplicationDate(simpleDateFormat.parse(patentData.getApplicationDate()));
            }
            if (!StringUtils.isEmpty(patentData.getOpenDate())){
                patent.setOpenDate(simpleDateFormat.parse(patentData.getOpenDate()));
            }
            patent.setApplicationId(patentData.getApplicationId());
            patent.setOpenId(patentData.getOpenId());
            patent.setApplicant(patentData.getApplicant());
            patent.setCoApplicants(patentData.getCoApplicants());
            patent.setInventor(patentData.getInventor());
            patent.setViewCount(patentData.getViewCount());
            if (!StringUtils.isEmpty(patentData.getEnterCountryDate())){
                patent.setEnterCountryDate(simpleDateFormat.parse(patentData.getEnterCountryDate()));
            }
            patent.setAgency(patentData.getAgency());
            patent.setOriginalApplicationId(patentData.getOriginalApplicationId());
            patent.setProvinceId(patentData.getProvinceId());
            patent.setSummary(patentData.getSummary());
            patent.setMainClain(patentData.getMainClain());
            patent.setPages(patentData.getPages());
            patent.setMainClassification(patentData.getMainClassification());
            patent.setPatentClassification(patentData.getPatentClassification());
            patent.setDomain(patentData.getDomain());

            patentService.save(patent);

            patent = this.existPatent(patentService, patentData.getKgId());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            BlockChainResource resource = new BlockChainResource("0", "20", "null",
                    "Patent_"+patent.getId(), formatter.format(patent.getGmtCreate()), "Admin_1");
            BlockChainController.setBlockChainResource(resource);
        }
    }

    private SmartcityPatent existPatent(ResourceService<SmartcityPatent> service, String kgId){
        QueryWrapper<SmartcityPatent> wrapper = new QueryWrapper<>();
        wrapper.eq("kg_id", kgId);
        SmartcityPatent patent = service.getOne(wrapper);
        return patent;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
