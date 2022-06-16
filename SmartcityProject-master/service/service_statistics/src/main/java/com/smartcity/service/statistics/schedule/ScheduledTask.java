package com.smartcity.service.statistics.schedule;

import com.smartcity.service.statistics.service.DailyService;
import com.smartcity.service.statistics.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    private DailyService dailyService;

    /**
     * 每天凌晨1点执行定时
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void task(){
        //获取前一天的日期
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        dailyService.registerCount(day);
    }

    /*
    //每隔5秒执行一次
    @Scheduled(cron = "0/5 * * * * ?")
    public void test(){
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        System.out.println("每隔五秒执行一次");
        System.out.println(day);
    }
    */
}
