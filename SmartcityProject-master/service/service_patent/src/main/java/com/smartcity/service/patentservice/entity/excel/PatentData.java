package com.smartcity.service.patentservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PatentData {
    @ExcelProperty(index = 0)
    private String kgId;

    @ExcelProperty(index = 1)
    private String url;

    @ExcelProperty(index = 2)
    private String title;

    @ExcelProperty(index = 3)
    private String applicationDate;

    @ExcelProperty(index = 4)
    private String openDate;

    @ExcelProperty(index = 5)
    private String applicationId;

    @ExcelProperty(index = 6)
    private String openId;

    @ExcelProperty(index = 7)
    private String applicant;

    @ExcelProperty(index = 8)
    private String coApplicants;

    @ExcelProperty(index = 9)
    private String inventor;

    @ExcelProperty(index = 10)
    private String viewCount;

    @ExcelProperty(index = 11)
    private String enterCountryDate;

    @ExcelProperty(index = 12)
    private String agency;

    @ExcelProperty(index = 13)
    private String originalApplicationId;

    @ExcelProperty(index = 14)
    private String provinceId;

    @ExcelProperty(index = 15)
    private String summary;

    @ExcelProperty(index = 16)
    private String mainClain;

    @ExcelProperty(index = 17)
    private Integer pages;

    @ExcelProperty(index = 18)
    private String mainClassification;

    @ExcelProperty(index = 19)
    private String patentClassification;

    @ExcelProperty(index = 20)
    private String domain;
}
