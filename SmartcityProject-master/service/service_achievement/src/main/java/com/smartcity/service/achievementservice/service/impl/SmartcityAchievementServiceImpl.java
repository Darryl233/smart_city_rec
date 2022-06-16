package com.smartcity.service.achievementservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.smartcity.service.achievementservice.entity.SmartcityAchievement;
import com.smartcity.service.achievementservice.entity.excel.AchievementData;
import com.smartcity.service.achievementservice.listener.AchievementExcelListener;
import com.smartcity.service.achievementservice.mapper.SmartcityAchievementMapper;
import com.smartcity.service.achievementservice.service.SmartcityAchievementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * <p>
 * 技术成果 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-15
 */
@Service
public class SmartcityAchievementServiceImpl extends ServiceImpl<SmartcityAchievementMapper, SmartcityAchievement>
        implements SmartcityAchievementService {

    @Override
    public void saveByExcel(MultipartFile file, SmartcityAchievementService service) {
        try{
            InputStream inputStream = file.getInputStream();

            EasyExcel.read(inputStream, AchievementData.class, new AchievementExcelListener(service)).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
