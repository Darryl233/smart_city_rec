package com.smartcity.resources_crud.client;

import com.smartcity.common.commonutils.entity.CommonHistory;
import com.smartcity.resources_crud.client.Impl.DegradeFeignClient;
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