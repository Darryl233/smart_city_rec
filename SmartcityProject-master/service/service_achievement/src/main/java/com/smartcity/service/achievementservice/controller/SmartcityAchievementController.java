package com.smartcity.service.achievementservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartcity.common.commonutils.BlockChainController;
import com.smartcity.common.commonutils.JwtUtils;
import com.smartcity.common.commonutils.R;
import com.smartcity.common.commonutils.entity.BlockChainResource;
import com.smartcity.common.commonutils.entity.CommonHistory;
import com.smartcity.service.achievementservice.client.UserHistoryClient;
import com.smartcity.service.achievementservice.entity.SmartcityAchievement;
import com.smartcity.service.achievementservice.entity.vo.AchievementQuery;
import com.smartcity.service.achievementservice.service.SmartcityAchievementService;
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
 * 技术成果 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-15
 */
@Api(description = "成果管理")
@RestController
@RequestMapping("/achievementservice/achievement")
public class SmartcityAchievementController {
    @Autowired
    private SmartcityAchievementService achievementService;

    @Autowired
    private UserHistoryClient historyClient;

    @ApiOperation(value = "通过上传excel文件，添加成果信息")
    @PostMapping("/import")
    public R addAchievementByExcel(MultipartFile file){
        achievementService.saveByExcel(file, achievementService);
        return R.ok();
    }

    @ApiOperation(value = "查询所有成果")
    @GetMapping("/findAll")
    public R findAll(){
        List<SmartcityAchievement> achievementList = achievementService.list(null);
        return R.ok().data("items", achievementList);
    }

    @ApiOperation(value = "根据id，逻辑删除成果")
    @DeleteMapping("/remove/{id}")
    public R removeById(
            @ApiParam(name = "id",value = "成果id",required = true)
            @PathVariable String id){
        boolean flag = achievementService.removeById(id);
        if(flag){
            return R.ok();
        }else{
            return R.error();
        }
    }

    //使用@RequestBody，接收前端json数据，需要用post
    @ApiOperation(value = "条件分页查询成果列表")
    @PostMapping("/pageAchievementCondition/{page}/{limit}")
    public R pageAchievementCondition(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "condition", value = "条件", required = false)
            @RequestBody(required = false) AchievementQuery achievementQuery){
        //创建page对象
        Page<SmartcityAchievement> pageParam = new Page<>(page, limit);
        //构建条件wrapper
        QueryWrapper<SmartcityAchievement> wrapper = new QueryWrapper<>();
        //多条件组合查询
        if (achievementQuery != null) {
            String title = achievementQuery.getTitle();
            String author = achievementQuery.getAuthor();
            String mechanism = achievementQuery.getMechanism();
            String summary = achievementQuery.getSummary();
            String keyword = achievementQuery.getKeyword();
            String price_left = achievementQuery.getPrice_left();
            String price_right = achievementQuery.getPrice_right();
            String begin = achievementQuery.getBegin();
            String end = achievementQuery.getEnd();
            if (!StringUtils.isEmpty(title)) {
                wrapper.like("title", title);
            }
            if (!StringUtils.isEmpty(author)) {
                wrapper.like("author", author);
            }
            if (!StringUtils.isEmpty(mechanism)) {
                wrapper.like("mechanism", mechanism);
            }
            if (!StringUtils.isEmpty(summary)) {
                wrapper.like("summary", summary);
            }
            if (!StringUtils.isEmpty(keyword)) {
                wrapper.like("keywords", keyword);
            }
            if (!StringUtils.isEmpty(price_left)) {
                wrapper.ge("price", price_left);
            }
            if (!StringUtils.isEmpty(price_right)) {
                wrapper.le("price", price_right);
            }
            if (!StringUtils.isEmpty(begin)) {
                wrapper.ge("year", begin);
            }
            if (!StringUtils.isEmpty(end)) {
                wrapper.le("year", end);
            }
        }
        //排序
        wrapper.orderByDesc("id");
        //调用方法实现条件查询
        achievementService.page(pageParam, wrapper);
        List<SmartcityAchievement> records = pageParam.getRecords();
        long total = pageParam.getTotal();

        return  R.ok().data("total", total).data("rows", records);
    }

    //使用@RequestBody，接受前端json，需用post
    @ApiOperation(value = "添加成果")
    @PostMapping("/addAchievement")
    public R addAchievement(
            @ApiParam(name = "achievement", value = "成果", required = true)
            @RequestBody(required = true) SmartcityAchievement achievement,
            HttpServletRequest request
    ){
        boolean save = achievementService.save(achievement);
        if(save){
            if (request.getHeader("admin")!=null){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                BlockChainResource resource = new BlockChainResource("0", "20", "null",
                        "Achievement_"+achievement.getId(), formatter.format(achievement.getGmtCreate()), "Admin_1");
                BlockChainController.setBlockChainResource(resource);
            }
            return R.ok().data("achievement", achievement);
        }
        else{
            return R.error();
        }
    }

    @ApiOperation(value = "根据ID查询成果")
    @GetMapping("/getAchievement/{id}")
    public R getAchievement(
            @ApiParam(name = "id", value = "成果id", required = true)
            @PathVariable String id,
            HttpServletRequest request
    ){
        SmartcityAchievement achievement = achievementService.getById(id);
        System.out.println(achievement);
        if (request.getHeader("admin") != null){
            return R.ok().data("achievement", achievement);
        }

        //调用jwt工具类，根据request获取头信息，返回用户id,不是user_id
        String logId = JwtUtils.getMemberIdByJwtToken(request);
        CommonHistory history = new CommonHistory();
        history.setLogId(logId);
        history.setResourceType(6);
        history.setKgId(achievement.getKgId());
        //boolean saveHistory = historyClient.saveHistory(history);
        boolean saveHistory = true;
        if (saveHistory)
            return R.ok().data("achievement", achievement);
        else
            return R.ok().message("添加浏览记录失败").data("achievement", achievement);
    }

    //使用@RequestBody，接受前端json，需用post
    @ApiOperation(value = "修改成果")
    @PostMapping("/updateAchievement")
    public R updateAchievement(
            @ApiParam(name = "achievement", value = "成果", required = true)
            @RequestBody(required = true) SmartcityAchievement achievement
    ){
        boolean update = achievementService.updateById(achievement);
        if(update){
            return R.ok().data("achievement", achievement);
        }
        else{
            return R.error();
        }
    }

    @ApiOperation(value = "根据Id获取成果batch")
    @GetMapping("/getAchievementBatch")
    public R getBatch(@RequestParam("IDList") List<String> IDList){
        List<SmartcityAchievement> achievementList = new ArrayList<>();
        QueryWrapper<SmartcityAchievement> wrapper = new QueryWrapper<>();
        for(int i=0; i<IDList.size(); i++){
            wrapper.ne("kg_id", IDList.get(i));
            QueryWrapper<SmartcityAchievement> tmp = new QueryWrapper<>();
            tmp.eq("kg_id", IDList.get(i));
            SmartcityAchievement achievement = achievementService.getOne(tmp);
            achievementList.add(achievement);
        }
        if (IDList.size()<10){
            wrapper.last("limit "+String.valueOf(10-IDList.size()));
            achievementList.addAll(achievementService.list(wrapper));
        }
        return R.ok().data("items", achievementList);
    }
}

