package com.smartcity.service.domainservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DomainData {

    @ExcelProperty(index = 0)
    private String firstdomain;

    @ExcelProperty(index = 1)
    private String seconddomain;

    @ExcelProperty(index = 2)
    private String depict;
}
