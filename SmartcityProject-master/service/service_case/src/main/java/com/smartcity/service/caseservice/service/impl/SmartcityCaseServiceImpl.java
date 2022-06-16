package com.smartcity.service.caseservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.smartcity.service.caseservice.entity.SmartcityCase;
import com.smartcity.service.caseservice.entity.excel.CaseData;
import com.smartcity.service.caseservice.listener.CaseExcelListener;
import com.smartcity.service.caseservice.mapper.SmartcityCaseMapper;
import com.smartcity.service.caseservice.service.SmartcityCaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * <p>
 * 案例 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-26
 */
@Service
public class SmartcityCaseServiceImpl extends ServiceImpl<SmartcityCaseMapper, SmartcityCase> implements SmartcityCaseService {
    @Override
    public void saveByExcel(MultipartFile file, SmartcityCaseService service) {
        try{
            //文件输入流
            InputStream in = file.getInputStream();

            //调用方法进行读取
            EasyExcel.read(in, CaseData.class, new CaseExcelListener(service)).sheet().doRead();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
