package com.smartcity.service.patentservice.client;

import com.smartcity.common.commonutils.entity.CommonHistory;
import com.smartcity.service.patentservice.client.Impl.DegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "service-ucenter", fallback = DegradeFeignClient.class)
public interface UserHistoryClient {

    @PostMapping(value = "/ucenter/userhistory/saveHistory")
    boolean saveHistory(@RequestBody CommonHistory history);
}
