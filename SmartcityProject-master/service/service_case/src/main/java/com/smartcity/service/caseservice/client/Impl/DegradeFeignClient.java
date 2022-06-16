package com.smartcity.service.caseservice.client.Impl;

import com.smartcity.common.commonutils.entity.CommonHistory;
import com.smartcity.service.caseservice.client.UserHistoryClient;

public class DegradeFeignClient implements UserHistoryClient {
    @Override
    public boolean saveHistory(CommonHistory history) {
        return false;
    }
}
