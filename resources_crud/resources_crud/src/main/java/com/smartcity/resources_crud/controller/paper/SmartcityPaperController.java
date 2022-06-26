package com.smartcity.resources_crud.controller.paper;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartcity.common.commonutils.BlockChainController;
import com.smartcity.common.commonutils.JwtUtils;
import com.smartcity.common.commonutils.R;
import com.smartcity.common.commonutils.entity.BlockChainResource;
import com.smartcity.common.commonutils.entity.CommonHistory;
import com.smartcity.resources_crud.client.UserHistoryClient;
import com.smartcity.resources_crud.entity.paper.SmartcityPaper;
import com.smartcity.resources_crud.entity.paper.vo.PaperQuery;
import com.smartcity.resources_crud.service.paper.impl.SmartcityPaperServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 论文 前端控制器
 */
@RestController
@RequestMapping("/paperservice/paper")
@Api(description = "论文管理")
public class SmartcityPaperController {

    @Autowired
    private SmartcityPaperServiceImpl paperService;

    @Autowired
    private UserHistoryClient historyClient;

    //通过excel来添加paper信息
    //获取上传文件
    @ApiOperation(value = "通过excel文件进行导入")
    @PostMapping("/import")
    public R addPaperByExcel(MultipartFile file){
        paperService.saveByExcel(file, paperService);
        return R.ok();
    }

    //查询论文表所有数据
    //rest风格
    @ApiOperation(value = "查询所有论文")
    @GetMapping("/findAll")
    public R findAllPaper(){
        List<SmartcityPaper> paperList = paperService.list(null);
        return R.ok().data("items", paperList);
    }

    @ApiOperation(value = "根据id,逻辑删除论文")
    @DeleteMapping("remove/{id}")
    public R removeById(
            @ApiParam(name = "id",value = "论文id",required = true)
            @PathVariable String id){
        boolean flag = paperService.removeById(id);
        if(flag){
            return R.ok();
        }else{
            return R.error();
        }
    }


    //条件分页查询
    //使用@RequestBody，接收前端json数据，需要用post
    @ApiOperation(value = "条件分页论文列表")
    @PostMapping("pagePaperCondition/{page}/{limit}")
    public R pagePaperCondition(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "condition", value = "条件", required = false)
            @RequestBody(required = false) PaperQuery paperQuery){
        //创建page对象
        Page<SmartcityPaper> pageParam = new Page<>(page, limit);
        //构建条件wrapper
        QueryWrapper<SmartcityPaper> wrapper = new QueryWrapper<>();
        //多条件组合查询
        //判断条件值是否为空，如果不为空则拼接条件
        String title = paperQuery.getTitle();
        String author = paperQuery.getAuthor();
        String summary = paperQuery.getSummary();
        String keyword = paperQuery.getKeyword();
        String price_left = paperQuery.getPrice_left();
        String price_right = paperQuery.getPrice_right();
        String begin = paperQuery.getBegin();
        String end = paperQuery.getEnd();
        if(!StringUtils.isEmpty(title)){
            wrapper.like("title", title);
        }
        if(!StringUtils.isEmpty(author)){
            wrapper.like("author", author);
        }
        if(!StringUtils.isEmpty(summary)){
            wrapper.like("summary", summary);
        }
        if(!StringUtils.isEmpty(keyword)){
            wrapper.like("keywords", keyword);
        }
        if(!StringUtils.isEmpty(price_left)){
            wrapper.ge("price", price_left);
        }
        if(!StringUtils.isEmpty(price_right)){
            wrapper.le("price", price_right);
        }
        if(!StringUtils.isEmpty(begin)){
            wrapper.ge("pub_date", begin);
        }
        if(!StringUtils.isEmpty(end)){
            wrapper.le("pub_date", end);
        }
        //排序
        wrapper.orderByDesc("cited");
        //调用方法实现条件查询
        paperService.page(pageParam, wrapper);
        List<SmartcityPaper> records = pageParam.getRecords();
        long total = pageParam.getTotal();

        return  R.ok().data("total", total).data("rows", records);
    }

    //添加论文
    //使用@RequestBody，接收前端json数据，需要用post
    /**
     * {
     *   "author": "string123",
     *   "cited": 0,
     *   "classification": "string",
     *   "download": 0,
     *   "file": "string",
     *   "keywords": "string",
     *   "mechanism": "string",
     *   "price": 0,
     *   "pubDate": "2020-11-03 11:36:50",
     *   "summary": "string",
     *   "title": "string123123",
     *   "url": "string"
     * }
     * */
    @ApiOperation(value = "添加论文")
    @PostMapping("/addPaper")
    public R addPaper(
            @ApiParam(name = "paper", value = "论文", required = true)
            @RequestBody(required = true) SmartcityPaper paper,
            HttpServletRequest request
    ){
        //调用方法实现添加
        boolean save = paperService.save(paper);
        if(save){
            if (request.getHeader("admin")!=null){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                BlockChainResource resource = new BlockChainResource("0", "20", "null",
                        "Paper_"+paper.getId(), formatter.format(paper.getGmtCreate()), "Admin_1");
                BlockChainController.setBlockChainResource(resource);
            }
            return R.ok().data("paper", paper);
        }
        else{
            return R.error();
        }
    }

    //根据ID查询论文
    @ApiOperation(value = "ID查询论文")
    @GetMapping("/getPaper/{id}")
    public R getPaperById(
            @ApiParam(name = "id", value = "论文ID", required = true)
            @PathVariable String id,
            HttpServletRequest request){

        //调用方法实现查询
        SmartcityPaper paper = paperService.getById(id);

        if (request.getHeader("admin") != null){
            return R.ok().data("paper", paper);
        }

        //调用jwt工具类，根据request获取头信息，返回用户id,不是user_id
        String logId = JwtUtils.getMemberIdByJwtToken(request);
        CommonHistory history = new CommonHistory();
        history.setLogId(logId);
        history.setResourceType(7);
        history.setKgId(paper.getKgId());
        boolean saveHistory = historyClient.saveHistory(history);
        if (saveHistory)
            return R.ok().data("paper", paper);
        else
            return R.ok().message("添加浏览记录失败").data("paper", paper);
    }

    //修改论文
    //使用@RequestBody，接收前端json数据，需要用post
    @ApiOperation(value = "修改论文详情")
    @PostMapping("/updatePaper")
    public R updatePaper(
            @ApiParam(name = "paper", value = "论文", required = true)
            @RequestBody(required = true) SmartcityPaper paper){

        //调用方法实现修改
        boolean update = paperService.updateById(paper);
        if(update){
            return R.ok();
        }
        else{
            return R.error();
        }
    }

    //根据ID获取论文batch
    @ApiOperation(value = "根据ID获取论文batch，自动补全到10个")
    @GetMapping("/getPaperBatch")
    public R getBatch(@RequestParam("IDList") List<String> IDList){
        //System.out.println(IDList);
        List<SmartcityPaper> paperList = new ArrayList<>();
        QueryWrapper<SmartcityPaper> wrapper = new QueryWrapper<>();
        for (int i = 0; i<IDList.size(); i++){
            wrapper.ne("kg_id", IDList.get(i));
            QueryWrapper<SmartcityPaper> tmp = new QueryWrapper<>();
            tmp.eq("kg_id", IDList.get(i));
            SmartcityPaper paper = paperService.getOne(tmp);
            paperList.add(paper);
        }
        if (IDList.size()<10){
            wrapper.last("limit " + String.valueOf(10-IDList.size()));
            paperList.addAll(paperService.list(wrapper));
        }
        return R.ok().data("items", paperList);
    }
}

