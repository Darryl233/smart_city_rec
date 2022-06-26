package com.smartcity.resources_crud.controller.patent;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartcity.common.commonutils.BlockChainController;
import com.smartcity.common.commonutils.JwtUtils;
import com.smartcity.common.commonutils.R;
import com.smartcity.common.commonutils.entity.BlockChainResource;
import com.smartcity.common.commonutils.entity.CommonHistory;
import com.smartcity.resources_crud.client.UserHistoryClient;
import com.smartcity.resources_crud.entity.patent.SmartcityPatent;
import com.smartcity.resources_crud.entity.patent.vo.PatentQuery;
import com.smartcity.resources_crud.service.patent.impl.SmartcityPatentServiceImpl;
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
 * <p>
 * 专利 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-14
 */
@RestController
@RequestMapping("/patentservice/patent")
@Api(description = "专利管理")
public class SmartcityPatentController {

    @Autowired
    private SmartcityPatentServiceImpl patentService;

    @Autowired
    private UserHistoryClient historyClient;

    //通过上传文件excel添加patent信息
    @PostMapping("/import")
    public R addPatentByExcel(MultipartFile file){
        patentService.saveByExcel(file, patentService);
        return R.ok();
    }

    //查询所有专利信息
    @ApiOperation(value = "查询所有专利")
    @GetMapping("/findAll")
    public R findAllPatent(){
        List<SmartcityPatent> patentList = patentService.list(null);
        return R.ok().data("items", patentList);
    }

    //根据id，逻辑删除专利
    @ApiOperation(value = "根据id,逻辑删除专利")
    @DeleteMapping("/remove/{id}")
    public R removeById(
            @ApiParam(name = "id",value = "专利id",required = true)
            @PathVariable String id){
        boolean flag = patentService.removeById(id);
        if(flag){
            return R.ok();
        }else{
            return R.error();
        }
    }

    //条件分页查询
    @ApiOperation(value = "条件分页查询专利")
    @PostMapping("/pagePatentCondition/{page}/{limit}")
    public R pagePatentCondition(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "condition", value = "条件", required = false)
            @RequestBody(required = false) PatentQuery patentQuery
    ){
        //创建page对象
        Page<SmartcityPatent> patentPage = new Page<>(page, limit);
        //构建条件wrapper
        QueryWrapper<SmartcityPatent> wrapper = new QueryWrapper<>();

        String title = patentQuery.getTitle();
        String summary = patentQuery.getSummary();
        String applicant = patentQuery.getApplicant();
        String application_begin = patentQuery.getApplication_begin();
        String application_end = patentQuery.getApplication_end();
        String open_begin = patentQuery.getOpen_begin();
        String open_end = patentQuery.getOpen_end();
        String price_left = patentQuery.getPrice_left();
        String price_right = patentQuery.getPrice_right();
        if(!StringUtils.isEmpty(title)){
            wrapper.like("title", title);
        }
        if(!StringUtils.isEmpty(summary)){
            wrapper.like("summary", summary);
        }
        if(!StringUtils.isEmpty(applicant)){
            wrapper.like("applicant", applicant);
        }
        if(!StringUtils.isEmpty(application_begin)){
            wrapper.ge("application_date", application_begin);
        }
        if(!StringUtils.isEmpty(application_end)){
            wrapper.le("application_date", application_end);
        }
        if(!StringUtils.isEmpty(open_begin)){
            wrapper.ge("open_date", open_begin);
        }
        if(!StringUtils.isEmpty(open_end)){
            wrapper.le("open_date", open_end);
        }
        if(!StringUtils.isEmpty(price_left)){
            wrapper.ge("price", price_left);
        }
        if(!StringUtils.isEmpty(price_right)){
            wrapper.le("price", price_right);
        }
        //排序
        wrapper.orderByDesc("gmt_create");
        //查询结果
        patentService.page(patentPage, wrapper);
        List<SmartcityPatent> records = patentPage.getRecords();
        long total = patentPage.getTotal();

        return R.ok().data("total", total).data("rows", records);
    }

    //添加专利
    @ApiOperation(value = "添加专利")
    @PostMapping("/addPatent")
    public R addPatent(
            @ApiParam(name = "patent", value = "专利", required = true)
            @RequestBody(required = true) SmartcityPatent patent,
            HttpServletRequest request
    ){
        boolean save = patentService.save(patent);
        if(save){
            if (request.getHeader("admin")!=null){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                BlockChainResource resource = new BlockChainResource("0", "20", "null",
                        "Patent_"+patent.getId(), formatter.format(patent.getGmtCreate()), "Admin_1");
                BlockChainController.setBlockChainResource(resource);
            }
            return R.ok().data("patent", patent);
        }else {
            return R.error().message("添加专利失败");
        }
    }

    //根据id查询专利
    @ApiOperation(value = "根据id查询专利")
    @GetMapping("/getPatent/{id}")
    public R getPatentById(
            @ApiParam(name = "id", value = "专利ID", required = true)
            @PathVariable String id,
            HttpServletRequest request){

        SmartcityPatent patent = patentService.getById(id);

        if (request.getHeader("admin") != null){
            return R.ok().data("patent", patent);
        }

        //调用jwt工具类，根据request获取头信息，返回用户id,不是user_id
        String logId = JwtUtils.getMemberIdByJwtToken(request);
        CommonHistory history = new CommonHistory();
        history.setLogId(logId);
        history.setResourceType(8);
        history.setKgId(patent.getKgId());
        boolean saveHistory = historyClient.saveHistory(history);
        if (saveHistory)
            return R.ok().data("patent", patent);
        else
            return R.ok().message("添加浏览记录失败").data("patent", patent);
    }

    //修改专利
    //使用@RequestBody，接收前端json数据，需要用post
    @ApiOperation(value = "修改专利详情")
    @PostMapping("/updatePatent")
    public R updatePatent(
            @ApiParam(name = "patent", value = "专利", required = true)
            @RequestBody(required = true) SmartcityPatent patent
    ){
        boolean update = patentService.updateById(patent);
        if (update){
            return R.ok().data("patent", patent);
        }else {
            return R.error().message("修改专利失败");
        }
    }

    //根据IDList获取专利batch
    @ApiOperation(value = "根据IDList获取专利batch，自动补全到10个")
    @GetMapping("/getPatentBatch")
    public R getBatch(@RequestParam("IDList") List<String> IDList){
        List<SmartcityPatent> patentList = new ArrayList<>();
        QueryWrapper<SmartcityPatent> wrapper = new QueryWrapper<>();
        for(int i=0; i<IDList.size(); i++){
            wrapper.ne("ke_id", IDList.get(i));
            QueryWrapper<SmartcityPatent> tmp = new QueryWrapper<>();
            tmp.eq("kg_id", IDList.get(i));
            SmartcityPatent patent = patentService.getOne(tmp);
            patentList.add(patent);
        }
        if (IDList.size()<10){
            wrapper.last("limit " + String.valueOf(10-IDList.size()));
            patentList.addAll(patentService.list(wrapper));
        }
        return R.ok().data("items", patentList);
    }
}

