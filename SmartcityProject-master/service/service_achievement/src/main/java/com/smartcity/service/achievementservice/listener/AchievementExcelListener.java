package com.smartcity.service.achievementservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.common.commonutils.BlockChainController;
import com.smartcity.common.commonutils.entity.BlockChainResource;
import com.smartcity.common.servicebase.exceptionhandler.CustomException;
import com.smartcity.service.achievementservice.entity.SmartcityAchievement;
import com.smartcity.service.achievementservice.entity.excel.AchievementData;
import com.smartcity.service.achievementservice.service.SmartcityAchievementService;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;

public class AchievementExcelListener extends AnalysisEventListener<AchievementData> {

    public SmartcityAchievementService achievementService;

    public AchievementExcelListener(){}
    public AchievementExcelListener(SmartcityAchievementService service){
        this.achievementService = service;
    }

    @SneakyThrows
    @Override
    public void invoke(AchievementData achievementData, AnalysisContext analysisContext) {
        if (achievementData == null){
            throw new CustomException(20001, "achievementData为空，即文件数据为空");
        }
        //一行一行读取
        SmartcityAchievement existAchievement = this.existAchievement(achievementService, achievementData.getKgId());
        if(existAchievement == null){
            SmartcityAchievement achievement = new SmartcityAchievement();
            achievement.setKgId(achievementData.getKgId());
            achievement.setTitle(achievementData.getTitle());
            achievement.setAuthor(achievementData.getAuthor());
            achievement.setMechanism(achievementData.getMechanism());
            achievement.setYear(achievementData.getYear());
            achievement.setNumber(achievementData.getNumber());
            achievement.setKeywords(achievementData.getKeywords());
            achievement.setSummary(achievementData.getSummary());
            achievement.setDomain(achievementData.getDomain());
            achievement.setUrl(achievementData.getUrl());
            achievementService.save(achievement);
            achievement = this.existAchievement(achievementService, achievementData.getKgId());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            BlockChainResource resource = new BlockChainResource("0", "20", "null",
                    "Achievement_"+achievement.getId(), formatter.format(achievement.getGmtCreate()), "Admin_1");
            BlockChainController.setBlockChainResource(resource);
        }
    }

    //判断成果标题是否唯一，避免重复添加
    private SmartcityAchievement existAchievement(SmartcityAchievementService service, String kgId){
        QueryWrapper<SmartcityAchievement> wrapper = new QueryWrapper<>();
        wrapper.eq("kg_id", kgId);
        SmartcityAchievement achievement = service.getOne(wrapper);
        return achievement;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
