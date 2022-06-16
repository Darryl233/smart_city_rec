package com.smartcity.service.statistics.service;

import com.smartcity.service.statistics.entity.Daily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-10
 */
public interface DailyService extends IService<Daily> {

    void registerCount(String day);

    Map<String, Object> getChartData(String begin, String end, String type);
}
