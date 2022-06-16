package com.smartcity.service.institutionservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.smartcity.service.institutionservice.entity.SmartcityInstitution;
import com.smartcity.service.institutionservice.entity.excel.InstitutionData;
import com.smartcity.service.institutionservice.listener.InstitutionExcelListener;
import com.smartcity.service.institutionservice.mapper.SmartcityInstitutionMapper;
import com.smartcity.service.institutionservice.service.SmartcityInstitutionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class SmartcityInstitutionServiceImpl extends ServiceImpl<SmartcityInstitutionMapper, SmartcityInstitution> implements SmartcityInstitutionService {

    @Override
    public void saveByExcel(MultipartFile file, SmartcityInstitutionService institutionService) {
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
