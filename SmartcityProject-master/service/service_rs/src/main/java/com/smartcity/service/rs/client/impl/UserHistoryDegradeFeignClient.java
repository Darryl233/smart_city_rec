package com.smartcity.service.rs.client.impl;

import com.smartcity.common.commonutils.entity.CommonHistory;
import com.smartcity.service.rs.client.UserHistoryClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserHistoryDegradeFeignClient implements UserHistoryClient {
    @Override
    public List<CommonHistory> getAllHistory() {
        return null;
    }
}
