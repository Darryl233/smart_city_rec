package com.smartcity.service.solutionservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.common.commonutils.BlockChainController;
import com.smartcity.common.commonutils.entity.BlockChainResource;
import com.smartcity.common.servicebase.exceptionhandler.CustomException;
import com.smartcity.service.solutionservice.entity.SmartcitySolution;
import com.smartcity.service.solutionservice.entity.excel.SolutionData;
import com.smartcity.service.solutionservice.service.SmartcitySolutionService;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;

public class SolutionExcelListener extends AnalysisEventListener<SolutionData> {
    public SmartcitySolutionService solutionService;

    public SolutionExcelListener(){}
    public SolutionExcelListener(SmartcitySolutionService service){
        this.solutionService = service;
    }

    @SneakyThrows
    @Override
    public void invoke(SolutionData solutionData, AnalysisContext analysisContext) {
        if (solutionData == null){
            throw new CustomException(2001, "SolutionData为空， 文件数据为空");
        }
        SmartcitySolution existSolution = this.existSolution(solutionService, solutionData.getKgId());
        if (existSolution == null){
            SmartcitySolution solution = new SmartcitySolution();
            solution.setKgId(solutionData.getKgId());
            solution.setUrl(solutionData.getUrl());
            solution.setTitle(solutionData.getTitle());
            solution.setRequirementNumber(solutionData.getRequirementNumber());
            solution.setOrgName(solutionData.getOrgName());
            solution.setOrgAddress(solutionData.getOrgAddress());
            solution.setPurchasePerson(solutionData.getPurchasePerson());
            solution.setPurchasePhone(solutionData.getPurchasePhone());
            solution.setBudget(solutionData.getBudget());
            solution.setKeywords(solutionData.getKeywords());
            solution.setDomain(solutionData.getDomain());
            solution.setIntro(solutionData.getIntro());
            solutionService.save(solution);
            solution = this.existSolution(solutionService, solutionData.getKgId());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            BlockChainResource resource = new BlockChainResource("0", "20", "null",
                    "Solution_"+solution.getId(), formatter.format(solution.getGmtCreate()), "Admin_1");
            BlockChainController.setBlockChainResource(resource);
        }
    }

    //判断KG_id是否唯一，不能重复添加
    private SmartcitySolution existSolution(SmartcitySolutionService service, String id){
        QueryWrapper<SmartcitySolution> wrapper = new QueryWrapper<>();
        wrapper.eq("kg_id", id);
        SmartcitySolution solution = service.getOne(wrapper);
        return solution;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
