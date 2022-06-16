package com.smartcity.service.paperservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.smartcity.service.paperservice.entity.SmartcityPaper;
import com.smartcity.service.paperservice.entity.excel.PaperData;
import com.smartcity.service.paperservice.listener.PaperExcelListener;
import com.smartcity.service.paperservice.mapper.SmartcityPaperMapper;
import com.smartcity.service.paperservice.service.SmartcityPaperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class SmartcityPaperServiceImpl extends ServiceImpl<SmartcityPaperMapper, SmartcityPaper> implements SmartcityPaperService {
    @Override
    public void saveByExcel(MultipartFile file, SmartcityPaperService service) {
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
