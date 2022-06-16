package com.smartcity.service.requirementservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.smartcity.service.requirementservice.entity.SmartcityRequirement;
import com.smartcity.service.requirementservice.entity.excel.RequirementData;
import com.smartcity.service.requirementservice.listener.RequirementExcelListener;
import com.smartcity.service.requirementservice.mapper.SmartcityRequirementMapper;
import com.smartcity.service.requirementservice.service.SmartcityRequirementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * <p>
 * 项目需求 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-26
 */
@Service
public class SmartcityRequirementServiceImpl extends ServiceImpl<SmartcityRequirementMapper, SmartcityRequirement> implements SmartcityRequirementService {

    @Override
    public void saveByExcel(MultipartFile file, SmartcityRequirementService requirementService) {
        try{
            //文件输入流
            InputStream in = file.getInputStream();

            //调用方法进行读取
            EasyExcel.read(in, RequirementData.class, new RequirementExcelListener(requirementService)).sheet().doRead();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
