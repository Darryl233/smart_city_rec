package com.smartcity.service.expertservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.smartcity.service.expertservice.entity.SmartcityExpert;
import com.smartcity.service.expertservice.entity.excel.ExpertData;
import com.smartcity.service.expertservice.listener.ExpertListener;
import com.smartcity.service.expertservice.mapper.SmartcityExpertMapper;
import com.smartcity.service.expertservice.service.SmartcityExpertService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class SmartcityExpertServiceImpl extends ServiceImpl<SmartcityExpertMapper, SmartcityExpert> implements SmartcityExpertService {

    @Override
    public void saveByExcel(MultipartFile file, SmartcityExpertService service) {
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
