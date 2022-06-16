package com.smartcity.service.rs.client.impl;

import com.smartcity.common.commonutils.R;
import com.smartcity.service.rs.client.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DegradeFeignClient implements ExpertClient, InstitutionClient, RequirementClient, SolutionClient, CaseClient, AchievementClient, PaperClient, PatentClient {
    @Override
    public R getBatch(List<String> IDList) {
        return null;
    }

    @Override
    public R findAll() {
        return null;
    }

    @Override
    public R get(String id) {
        return null;
    }
}
