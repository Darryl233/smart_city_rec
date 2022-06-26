package com.smartcity.resources_crud.service.requirement.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartcity.resources_crud.entity.requirement.SmartcityRequirement;
import com.smartcity.resources_crud.entity.requirement.excel.RequirementData;
import com.smartcity.resources_crud.listener.RequirementExcelListener;
import com.smartcity.resources_crud.mapper.requirement.SmartcityRequirementMapper;
import com.smartcity.resources_crud.service.ResourceService;
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
public class SmartcityRequirementServiceImpl extends ServiceImpl<SmartcityRequirementMapper, SmartcityRequirement> implements ResourceService<SmartcityRequirement> {

    @Override
    public void saveByExcel(MultipartFile file, ResourceService<SmartcityRequirement> requirementService) {
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
