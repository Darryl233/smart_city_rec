package com.smartcity.service.statistics.client.impl;

import com.smartcity.common.commonutils.R;
import com.smartcity.service.statistics.client.UcenterClient;
import org.springframework.stereotype.Component;

@Component
public class UcenterDegradeFeignClient implements UcenterClient {
    @Override
    public R countRegister(String day) {
        return R.error().message("服务熔断机制启动，当前服务繁忙，请稍后重试");
    }
}
