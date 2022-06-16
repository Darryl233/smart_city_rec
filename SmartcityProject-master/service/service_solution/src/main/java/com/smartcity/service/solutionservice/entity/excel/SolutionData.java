package com.smartcity.service.solutionservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SolutionData {

    @ExcelProperty(index = 0)
    private String kgId;

    @ExcelProperty(index = 1)
    private String url;

    @ExcelProperty(index = 2)
    private String title;

    @ExcelProperty(index = 3)
    private String requirementNumber;

    @ExcelProperty(index = 4)
    private String orgName;

    @ExcelProperty(index = 9)
    private String orgAddress;

    @ExcelProperty(index = 11)
    private String purchasePerson;

    @ExcelProperty(index = 13)
    private String purchasePhone;

    @ExcelProperty(index = 15)
    private String budget;

    @ExcelProperty(index = 16)
    private String keywords;

    @ExcelProperty(index = 17)
    private String domain;

    @ExcelProperty(index = 19)
    private String intro;
}
