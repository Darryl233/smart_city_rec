package com.smartcity.service.relationservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.smartcity.service.relationservice.entity.Relations;
import com.smartcity.service.relationservice.entity.excel.RelationData;
import com.smartcity.service.relationservice.listen.RelationExcelListener;
import com.smartcity.service.relationservice.mapper.RelationsMapper;
import com.smartcity.service.relationservice.service.RelationsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * <p>
 * 关系表：单位-提出-案例 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-12-28
 */
@Service
public class RelationsServiceImpl extends ServiceImpl<RelationsMapper, Relations> implements RelationsService {

    @Override
    public void saveByExcel(MultipartFile file, RelationsService service) {
        try{
            //文件输入流
            InputStream in = file.getInputStream();

            //调用方法进行读取
            EasyExcel.read(in, RelationData.class, new RelationExcelListener(service)).sheet().doRead();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
