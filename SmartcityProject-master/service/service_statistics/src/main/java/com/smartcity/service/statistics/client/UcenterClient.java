package com.smartcity.service.statistics.client;

import com.smartcity.common.commonutils.R;
import com.smartcity.service.statistics.client.impl.UcenterDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-ucenter", fallback = UcenterDegradeFeignClient.class)
public interface UcenterClient {
    @GetMapping("/ucenter/member/countRegister/{day}")
    public R countRegister(@PathVariable("day") String day);
}
