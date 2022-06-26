package com.smartcity.resources_crud.service.expert.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartcity.resources_crud.entity.expert.SmartcityExpert;
import com.smartcity.resources_crud.entity.expert.excel.ExpertData;
import com.smartcity.resources_crud.listener.ExpertListener;
import com.smartcity.resources_crud.mapper.expert.SmartcityExpertMapper;
import com.smartcity.resources_crud.service.ResourceService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * <p>
 * 专家 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-05
 */
@Service
public class SmartcityExpertServiceImpl extends ServiceImpl<SmartcityExpertMapper, SmartcityExpert> implements ResourceService<SmartcityExpert> {

    @Override
    public void saveByExcel(MultipartFile file, ResourceService<SmartcityExpert> service) {
        try{
            //文件输入流
            InputStream in = file.getInputStream();

            //调用方法进行读取
            EasyExcel.read(in, ExpertData.class, new ExpertListener(service)).sheet().doRead();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
