package com.smartcity.service.rs.client;

import com.smartcity.common.commonutils.R;
import com.smartcity.service.rs.client.impl.DegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "service-database", fallback = DegradeFeignClient.class)
public interface InstitutionClient {
    @GetMapping("/institutionservice/institution/getInstitutionBatch")
    public R getBatch(@RequestParam("IDList") List<String> IDList);

    @GetMapping(value = "/institutionservice/institution/findAll")
    public R findAll();
}
