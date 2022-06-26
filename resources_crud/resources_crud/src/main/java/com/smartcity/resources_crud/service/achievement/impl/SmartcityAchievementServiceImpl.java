package com.smartcity.resources_crud.service.achievement.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartcity.resources_crud.entity.achievement.SmartcityAchievement;
import com.smartcity.resources_crud.entity.achievement.excel.AchievementData;
import com.smartcity.resources_crud.listener.achievement.AchievementExcelListener;
import com.smartcity.resources_crud.mapper.achievement.SmartcityAchievementMapper;
import com.smartcity.resources_crud.service.ResourceService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
* @author bytedance
* @description 针对表【smartcity_achievement(技术成果)】的数据库操作Service实现
* @createDate 2022-04-13 18:01:04
*/
@Service
public class SmartcityAchievementServiceImpl extends ServiceImpl<SmartcityAchievementMapper, SmartcityAchievement>
    implements ResourceService<SmartcityAchievement> {

    @Override
    public void saveByExcel(MultipartFile file, ResourceService<SmartcityAchievement> service) {
        try{
            InputStream inputStream = file.getInputStream();

            EasyExcel.read(inputStream, AchievementData.class, new AchievementExcelListener(service)).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}




