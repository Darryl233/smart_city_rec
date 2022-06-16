package com.smartcity.service.institutionservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.common.servicebase.exceptionhandler.CustomException;
import com.smartcity.service.institutionservice.entity.SmartcityInstitution;
import com.smartcity.service.institutionservice.entity.excel.InstitutionData;
import com.smartcity.service.institutionservice.service.SmartcityInstitutionService;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;

public class InstitutionExcelListener extends AnalysisEventListener<InstitutionData> {

    public SmartcityInstitutionService institutionService;

    public InstitutionExcelListener(){}
    public InstitutionExcelListener(SmartcityInstitutionService service){this.institutionService = service;}

    @SneakyThrows
    @Override
    public void invoke(InstitutionData institutionData, AnalysisContext analysisContext) {
        if(institutionData == null){
            throw new CustomException(20001, "institutionData为空，文件数据为空");
        }
        SmartcityInstitution existInstitution = this.existInstitution(institutionService, institutionData.getKgId());
        if(existInstitution == null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SmartcityInstitution institution = new SmartcityInstitution();
            institution.setKgId(institutionData.getKgId());
            institution.setName(institutionData.getFullname());
            institution.setClassification(institutionData.getClassification());
            institution.setOrgaddress(institutionData.getOrgAddress());
            institution.setRegisteaddress(institutionData.getRegisteAddress());
            institution.setProvince(institutionData.getProvince());
            institution.setCity(institutionData.getCity());
            institution.setBusinessscope(institutionData.getBusinessScope());
            if (!StringUtils.isEmpty(institutionData.getCreateTime())){
                institution.setCreatetime(simpleDateFormat.parse(institutionData.getCreateTime()));
            }
            institution.setDomain(institutionData.getFieldTypeName());
            institution.setIndustry(institutionData.getIndustryName());
            institution.setPhone(institutionData.getOrgPhone());
            institution.setRegisteredcapital(institutionData.getRegisteredCapital());
            institution.setRegisterstatus(institutionData.getRegisterStatus());
            institution.setResearch(institutionData.getResearch());
            this.institutionService.save(institution);
        }
    }

    private SmartcityInstitution existInstitution(SmartcityInstitutionService service, String kgId){
        QueryWrapper<SmartcityInstitution> wrapper = new QueryWrapper<>();
        wrapper.eq("kg_id", kgId);
        SmartcityInstitution institution = service.getOne(wrapper);
        return institution;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
