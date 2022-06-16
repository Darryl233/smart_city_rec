package com.smartcity.service.requirementservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class RequirementData{

    @ExcelProperty(index = 0)
    private String kgId;

    @ExcelProperty(index = 1)
    private String url;

    @ExcelProperty(index = 2)
    private String title;

    @ExcelProperty(index = 3)
    private String requirementNumber;

    @ExcelProperty(index = 4)
    private String purchasePerson;

    @ExcelProperty(index = 5)
    private String purchasePhone;

    @ExcelProperty(index = 6)
    private String purchaseInstitution;

    @ExcelProperty(index = 7)
    private String purchaseOrgAddress;

    @ExcelProperty(index = 8)
    private String orgPhone;

    @ExcelProperty(index = 9)
    private String contentDescription;

    @ExcelProperty(index = 10)
    private String budget;

    @ExcelProperty(index = 11)
    private String keywords;

    @ExcelProperty(index = 12)
    private String domain;

    @ExcelProperty(index = 13)
    private String announceTime;

    @ExcelProperty(index = 14)
    private String beginTime;

    @ExcelProperty(index = 15)
    private String endTime;
}