package com.smartcity.service.solutionservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.smartcity.service.solutionservice.entity.SmartcitySolution;
import com.smartcity.service.solutionservice.entity.excel.SolutionData;
import com.smartcity.service.solutionservice.listener.SolutionExcelListener;
import com.smartcity.service.solutionservice.mapper.SmartcitySolutionMapper;
import com.smartcity.service.solutionservice.service.SmartcitySolutionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * <p>
 * 解决方案 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-26
 */
@Service
public class SmartcitySolutionServiceImpl extends ServiceImpl<SmartcitySolutionMapper, SmartcitySolution> implements SmartcitySolutionService {

    @Override
    public void saveByExcel(MultipartFile file, SmartcitySolutionService service) {
        try{
            //文件输入流
            InputStream in = file.getInputStream();

            //调用方法进行读取
            EasyExcel.read(in, SolutionData.class, new SolutionExcelListener(service)).sheet().doRead();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
