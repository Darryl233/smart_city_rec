package com.smartcity.service.ucenter.client;

import com.smartcity.common.commonutils.R;
import com.smartcity.common.commonutils.entity.Expert;
import com.smartcity.common.commonutils.entity.Institution;
import com.smartcity.service.ucenter.client.impl.ExpertDegradeFeignClient;
import com.smartcity.service.ucenter.client.impl.InstitutionDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//服务熔断机制
@Component
@FeignClient(name = "service-database", fallback = InstitutionDegradeFeignClient.class)
public interface InstitutionClient {

    @PostMapping("/institutionservice/institution/addInstitutionByRegister")
    public Institution addInstitutionByRegister(@RequestBody Institution institution);

    @GetMapping("/institutionservice/institution/getInstitutionBatch")
    public R getBatch(@RequestParam("IDList") List<String> IDList);
}