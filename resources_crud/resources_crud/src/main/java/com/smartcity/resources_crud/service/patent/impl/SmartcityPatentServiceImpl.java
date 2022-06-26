package com.smartcity.resources_crud.service.patent.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.smartcity.resources_crud.entity.patent.SmartcityPatent;
import com.smartcity.resources_crud.entity.patent.excel.PatentData;
import com.smartcity.resources_crud.listener.PatentExcelListener;
import com.smartcity.resources_crud.mapper.patent.SmartcityPatentMapper;
import com.smartcity.resources_crud.service.ResourceService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * <p>
 * 专利 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-14
 */
@Service
public class SmartcityPatentServiceImpl extends ServiceImpl<SmartcityPatentMapper, SmartcityPatent> implements ResourceService<SmartcityPatent> {

    @Override
    public void saveByExcel(MultipartFile file, ResourceService<SmartcityPatent> service) {
        try{
            //文件输入流
            InputStream in = file.getInputStream();

            //调用方法进行读取
            EasyExcel.read(in, PatentData.class, new PatentExcelListener(service)).sheet().doRead();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
