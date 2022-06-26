package com.smartcity.resources_crud.service.cases.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartcity.resources_crud.entity.achievement.SmartcityAchievement;
import com.smartcity.resources_crud.entity.cases.SmartcityCase;
import com.smartcity.resources_crud.entity.cases.excel.CaseData;
import com.smartcity.resources_crud.listener.cases.CaseExcelListener;
import com.smartcity.resources_crud.mapper.achievement.SmartcityAchievementMapper;
import com.smartcity.resources_crud.mapper.cases.SmartcityCaseMapper;
import com.smartcity.resources_crud.service.ResourceService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class SmartcityCaseServiceImpl extends ServiceImpl<SmartcityCaseMapper, SmartcityCase>
        implements ResourceService<SmartcityCase> {
    @Override
    public void saveByExcel(MultipartFile file, ResourceService<SmartcityCase> service) {
        try{
            //文件输入流
            InputStream in = file.getInputStream();

            //调用方法进行读取
            EasyExcel.read(in, CaseData.class, new CaseExcelListener(service)).sheet().doRead();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
