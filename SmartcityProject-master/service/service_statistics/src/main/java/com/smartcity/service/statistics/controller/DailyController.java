package com.smartcity.service.statistics.controller;


import com.smartcity.common.commonutils.R;
import com.smartcity.service.statistics.client.UcenterClient;
import com.smartcity.service.statistics.service.DailyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-10
 */
@RestController
@RequestMapping("/statistics/daily")
public class DailyController {

    @Autowired
    private DailyService dailyService;

    @ApiOperation("统计某一天的注册人数,生成统计数据")
    @PostMapping(value = "/registerCount/{day}")
    public R registerCount(@PathVariable String day){
        dailyService.registerCount(day);
        return R.ok();
    }

    @ApiOperation("获取统计数据")
    @GetMapping(value = "/showchart/{begin}/{end}/{type}")
    public R showChart(@PathVariable String begin, @PathVariable String end, @PathVariable String type){
        Map<String, Object> map = dailyService.getChartData(begin, end, type);
        return R.ok().data(map);
    }
}

