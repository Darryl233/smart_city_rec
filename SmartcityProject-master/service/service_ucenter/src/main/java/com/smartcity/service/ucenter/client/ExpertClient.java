package com.smartcity.service.ucenter.client;

import com.smartcity.common.commonutils.R;
import com.smartcity.common.commonutils.entity.Expert;
import com.smartcity.service.ucenter.client.impl.ExpertDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//服务熔断机制
@Component
@FeignClient(name = "service-database", fallback = ExpertDegradeFeignClient.class)
public interface ExpertClient {

    @PostMapping("/expertservice/expert/addExpertByRegister")
    public Expert addExpertByRegister(@RequestBody Expert expert);

    @GetMapping("/expertservice/expert/getExpertBatch")
    public R getBatch(@RequestParam("IDList") List<String> IDList);
}
