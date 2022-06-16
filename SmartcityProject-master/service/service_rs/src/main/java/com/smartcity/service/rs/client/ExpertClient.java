package com.smartcity.service.rs.client;

import com.smartcity.common.commonutils.R;
import com.smartcity.service.rs.client.impl.DegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//服务熔断机制
@Component
@FeignClient(name = "service-database", fallback = DegradeFeignClient.class)
public interface ExpertClient {

    @GetMapping("/expertservice/expert/getExpertBatch")
    public R getBatch(@RequestParam("IDList") List<String> IDList);

    @GetMapping("/expertservice/expert/{id}")
    public R get( @PathVariable String id);

    @GetMapping(value = "/expertservice/expert/findAll")
    public R findAll();
}
