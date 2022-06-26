package com.smartcity.resources_crud.client.Impl;

import com.smartcity.common.commonutils.entity.CommonHistory;
import com.smartcity.resources_crud.client.UserHistoryClient;
import org.springframework.stereotype.Component;

@Component
public class DegradeFeignClient implements UserHistoryClient {
    @Override
    public boolean saveHistory(CommonHistory history) {
        return false;
    }
}