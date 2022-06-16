package com.smartcity.service.domainservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.smartcity.service.domainservice.entity.Domain;
import com.smartcity.service.domainservice.entity.excel.DomainData;
import com.smartcity.service.domainservice.listener.DomainExcelListener;
import com.smartcity.service.domainservice.mapper.DomainMapper;
import com.smartcity.service.domainservice.service.DomainService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * <p>
 * 领域类别 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-05
 */
@Service
public class DomainServiceImpl extends ServiceImpl<DomainMapper, Domain> implements DomainService {

    @Override
    public void saveByExcel(MultipartFile file, DomainService domainService) {
        try{
            //文件输入流
            InputStream in = file.getInputStream();

            //调用方法进行读取
            EasyExcel.read(in, DomainData.class, new DomainExcelListener(domainService)).sheet().doRead();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
