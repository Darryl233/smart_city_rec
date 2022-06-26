package com.smartcity.resources_crud.service.solution.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartcity.resources_crud.entity.solution.SmartcitySolution;
import com.smartcity.resources_crud.entity.solution.excel.SolutionData;
import com.smartcity.resources_crud.listener.SolutionExcelListener;
import com.smartcity.resources_crud.mapper.solution.SmartcitySolutionMapper;
import com.smartcity.resources_crud.service.ResourceService;
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
public class SmartcitySolutionServiceImpl extends ServiceImpl<SmartcitySolutionMapper, SmartcitySolution> implements ResourceService<SmartcitySolution> {

    @Override
    public void saveByExcel(MultipartFile file, ResourceService<SmartcitySolution> service) {
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
