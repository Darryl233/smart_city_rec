package com.smartcity.service.ucenter.client;

import com.smartcity.common.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "service-database")
public interface PatentClient {

    @GetMapping("/patentservice/patent/getPatentBatch")
    public R getBatch(@RequestParam("IDList") List<String> IDList);
}
