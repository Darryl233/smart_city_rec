package com.smartcity.service.rs.client;

import com.smartcity.common.commonutils.entity.CommonHistory;
import com.smartcity.service.rs.client.impl.UserHistoryDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
@FeignClient(name = "service-ucenter", fallback = UserHistoryDegradeFeignClient.class)
public interface UserHistoryClient {
    @GetMapping(value = "/ucenter/userhistory/getAllHistory")
    public List<CommonHistory> getAllHistory();
}
