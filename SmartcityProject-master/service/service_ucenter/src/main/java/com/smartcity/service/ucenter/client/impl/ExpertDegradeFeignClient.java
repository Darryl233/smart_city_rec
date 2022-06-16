package com.smartcity.service.ucenter.client.impl;

import com.smartcity.common.commonutils.R;
import com.smartcity.common.commonutils.entity.Expert;
import com.smartcity.service.ucenter.client.ExpertClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExpertDegradeFeignClient implements ExpertClient {

    @Override
    public Expert addExpertByRegister(Expert expert) {
        return null;
    }

    @Override
    public R getBatch(List<String> IDList) {
        return null;
    }

}
