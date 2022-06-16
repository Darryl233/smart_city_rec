package com.smartcity.service.ucenter.client.impl;

import com.smartcity.common.commonutils.R;
import com.smartcity.common.commonutils.entity.Institution;
import com.smartcity.service.ucenter.client.InstitutionClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InstitutionDegradeFeignClient implements InstitutionClient {

    @Override
    public Institution addInstitutionByRegister(Institution institution) {
        return null;
    }

    @Override
    public R getBatch(List<String> IDList) {
        return null;
    }
}
