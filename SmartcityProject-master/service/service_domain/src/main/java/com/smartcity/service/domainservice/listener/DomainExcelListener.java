package com.smartcity.service.domainservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.common.servicebase.exceptionhandler.CustomException;
import com.smartcity.service.domainservice.entity.Domain;
import com.smartcity.service.domainservice.entity.excel.DomainData;
import com.smartcity.service.domainservice.service.DomainService;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;

public class DomainExcelListener extends AnalysisEventListener<DomainData> {

    public DomainService domainService;

    //无参和有参构造Listener，实现paperService注入
    public DomainExcelListener(){}
    public DomainExcelListener(DomainService domainService){
        this.domainService = domainService;
    }

    //判断domain标题是否唯一，不能重复添加
    private Domain existDomain(DomainService service, String name){
        QueryWrapper<Domain> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        Domain domain = service.getOne(wrapper);
        System.out.println(domain);
        return domain;
    }

    @SneakyThrows
    @Override
    public void invoke(DomainData domainData, AnalysisContext analysisContext) {
        System.out.println(domainData);
        if(domainData == null){
            throw new CustomException(20001, "paperData为空，文件数据为空");
        }
        //一行一行读取
        Domain existDomain = this.existDomain(domainService, domainData.getFirstdomain());
        if(existDomain == null){
            Domain domain = new Domain();
            domain.setTitle(domainData.getFirstdomain());
            domain.setParentId("0");
            existDomain = domain;
            domainService.save(domain);
        }
        String pid =  existDomain.getId();
        Domain secondDomain = new Domain();
        secondDomain.setTitle(domainData.getSeconddomain());
        secondDomain.setParentId(pid);
        secondDomain.setDepict(domainData.getDepict());
        domainService.save(secondDomain);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
