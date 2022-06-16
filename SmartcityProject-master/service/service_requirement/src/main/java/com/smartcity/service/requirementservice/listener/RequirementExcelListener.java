package com.smartcity.service.requirementservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.common.commonutils.BlockChainController;
import com.smartcity.common.commonutils.entity.BlockChainResource;
import com.smartcity.common.servicebase.exceptionhandler.CustomException;
import com.smartcity.service.requirementservice.entity.SmartcityRequirement;
import com.smartcity.service.requirementservice.entity.excel.RequirementData;
import com.smartcity.service.requirementservice.service.SmartcityRequirementService;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RequirementExcelListener extends AnalysisEventListener<RequirementData> {

    public SmartcityRequirementService requirementService;

    public RequirementExcelListener(){}
    public RequirementExcelListener(SmartcityRequirementService service){
        this.requirementService = service;
    }

    @SneakyThrows
    @Override
    public void invoke(RequirementData requirementData, AnalysisContext analysisContext) {
        if (requirementData == null){
            throw new CustomException(20001, "requirementData为空，文件数据为空");
        }
        SmartcityRequirement existRequirement = this.existRequirement(requirementService, requirementData.getKgId());
        if(existRequirement == null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SmartcityRequirement requirement = new SmartcityRequirement();
            requirement.setKgId(requirementData.getKgId());
            requirement.setUrl(requirementData.getUrl());
            requirement.setTitle(requirementData.getTitle());
            requirement.setRequirementNumber(requirementData.getRequirementNumber());
            requirement.setPurchasePerson(requirementData.getPurchasePerson());
            requirement.setPurchasePhone(requirementData.getPurchasePhone());
            requirement.setPurchaseInstitution(requirementData.getPurchaseInstitution());
            requirement.setPurchaseOrgAddress(requirementData.getPurchaseOrgAddress());
            requirement.setOrgPhone(requirementData.getOrgPhone());
            requirement.setContentDescription(requirementData.getContentDescription());
            requirement.setBudget(requirementData.getBudget());
            requirement.setKeywords(requirementData.getKeywords());
            if (!StringUtils.isEmpty(requirementData.getAnnounceTime())){
                requirement.setAnnounceTime(simpleDateFormat.parse(requirementData.getAnnounceTime()));
            }
            if (!StringUtils.isEmpty(requirementData.getBeginTime())){
                requirement.setBeginTime(simpleDateFormat.parse(requirementData.getBeginTime()));
            }
            if (!StringUtils.isEmpty(requirementData.getEndTime())){
                requirement.setEndTime(simpleDateFormat.parse(requirementData.getEndTime()));
            }
            requirement.setDomain(requirementData.getDomain());
            requirementService.save(requirement);
            requirement = this.existRequirement(requirementService, requirementData.getKgId());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            BlockChainResource resource = new BlockChainResource("0", "20", "null",
                    "Requirement_"+requirement.getId(), formatter.format(requirement.getGmtCreate()), "Admin_1");
            BlockChainController.setBlockChainResource(resource);
        }
    }

    //判断KG_id是否唯一，不能重复添加
    private SmartcityRequirement existRequirement(SmartcityRequirementService service, String id){
        QueryWrapper<SmartcityRequirement> wrapper = new QueryWrapper<>();
        wrapper.eq("kg_id", id);
        SmartcityRequirement requirement = service.getOne(wrapper);
        return requirement;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
