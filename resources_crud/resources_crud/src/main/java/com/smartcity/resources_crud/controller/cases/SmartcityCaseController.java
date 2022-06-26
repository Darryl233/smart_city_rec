package com.smartcity.resources_crud.controller.cases;

import com.smartcity.common.commonutils.R;
import com.smartcity.resources_crud.controller.CommonController;
import com.smartcity.resources_crud.entity.cases.SmartcityCase;
import com.smartcity.resources_crud.service.cases.impl.SmartcityCaseServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/caseservice/case")
public class SmartcityCaseController extends CommonController<SmartcityCase> {

    @Autowired
    private SmartcityCaseServiceImpl caseService;

    @ApiOperation(value = "查询所有案例")
    @GetMapping("/findAll")
    public R findAll(){
        List<SmartcityCase> list = caseService.list(null);
        return R.ok().data("items", list);
    }
}
