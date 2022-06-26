package com.smartcity.resources_crud.service.institution.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartcity.resources_crud.entity.institution.SmartcityInstitution;
import com.smartcity.resources_crud.entity.institution.excel.InstitutionData;
import com.smartcity.resources_crud.listener.InstitutionExcelListener;
import com.smartcity.resources_crud.mapper.institution.SmartcityInstitutionMapper;
import com.smartcity.resources_crud.service.ResourceService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * <p>
 * 单位 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-20
 */
@Service
public class SmartcityInstitutionServiceImpl extends ServiceImpl<SmartcityInstitutionMapper, SmartcityInstitution> implements ResourceService<SmartcityInstitution> {

    @Override
    public void saveByExcel(MultipartFile file, ResourceService<SmartcityInstitution> institutionService) {
        try{
            //文件输入流
            InputStream in = file.getInputStream();

            //调用方法进行读取
            EasyExcel.read(in, InstitutionData.class, new InstitutionExcelListener(institutionService)).sheet().doRead();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
