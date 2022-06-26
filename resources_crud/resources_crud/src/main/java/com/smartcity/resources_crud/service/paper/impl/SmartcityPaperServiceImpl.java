package com.smartcity.resources_crud.service.paper.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.smartcity.resources_crud.entity.paper.SmartcityPaper;
import com.smartcity.resources_crud.entity.paper.excel.PaperData;
import com.smartcity.resources_crud.listener.PaperExcelListener;
import com.smartcity.resources_crud.mapper.paper.SmartcityPaperMapper;
import com.smartcity.resources_crud.service.ResourceService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * <p>
 * 论文 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-05
 */
@Service
public class SmartcityPaperServiceImpl extends ServiceImpl<SmartcityPaperMapper, SmartcityPaper> implements ResourceService<SmartcityPaper> {
    @Override
    public void saveByExcel(MultipartFile file, ResourceService<SmartcityPaper> service) {
        try{
            //文件输入流
            InputStream in = file.getInputStream();

            //调用方法进行读取
            EasyExcel.read(in, PaperData.class, new PaperExcelListener(service)).sheet().doRead();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
