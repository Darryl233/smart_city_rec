package com.smartcity.resources_crud.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.common.commonutils.BlockChainController;
import com.smartcity.common.commonutils.entity.BlockChainResource;
import com.smartcity.common.servicebase.exceptionhandler.CustomException;
import com.smartcity.resources_crud.entity.paper.SmartcityPaper;
import com.smartcity.resources_crud.entity.paper.excel.PaperData;
import com.smartcity.resources_crud.service.ResourceService;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;


//Listener不能自动注入，不能交给spring进行管理，不能@Autowired
public class PaperExcelListener extends AnalysisEventListener<PaperData> {

    public ResourceService<SmartcityPaper> paperService;

    //无参和有参构造Listener，实现paperService注入
    public PaperExcelListener(){}
    public PaperExcelListener(ResourceService<SmartcityPaper> paperService){
        this.paperService = paperService;
    }

    @SneakyThrows
    @Override
    public void invoke(PaperData paperData, AnalysisContext analysisContext) {
        if(paperData == null){
            throw new CustomException(20001, "paperData为空，文件数据为空");
        }
        //一行一行读取
        SmartcityPaper existPaper = this.existPaper(paperService, paperData.getKgId());
        if(existPaper == null){
            SmartcityPaper paper = new SmartcityPaper();
            paper.setKgId(paperData.getKgId());
            paper.setTitle(paperData.getTitle());
            paper.setAuthor(paperData.getAuthor());
            paper.setMechanism(paperData.getMechanism());
            paper.setSummary(paperData.getSummary());
            paper.setKeywords(paperData.getKeywords());
            paper.setClassification((paperData.getClassification()));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (!StringUtils.isEmpty(paperData.getTime())){
                paper.setPubDate(simpleDateFormat.parse(paperData.getTime()));
            }
            paper.setCited(paperData.getCited());
            paper.setDownload(paperData.getDownload());
            paper.setUrl(paperData.getUrl());
            paper.setDomain(paperData.getDomain());
            paperService.save(paper);
            paper = this.existPaper(paperService, paperData.getKgId());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            BlockChainResource resource = new BlockChainResource("0", "20", "null",
                    "Paper_"+paper.getId(), formatter.format(paper.getGmtCreate()), "Admin_1");
            BlockChainController.setBlockChainResource(resource);
        }
    }

    //判断论文标题是否唯一，不能重复添加
    private SmartcityPaper existPaper(ResourceService<SmartcityPaper> service, String kgId){
        QueryWrapper<SmartcityPaper> wrapper = new QueryWrapper<>();
        wrapper.eq("kg_id", kgId);
        SmartcityPaper paper = service.getOne(wrapper);
        return paper;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
