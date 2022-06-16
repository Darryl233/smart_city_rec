package com.smartcity.service.caseservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class CaseData {
    @ExcelProperty(index = 0)
    private String kgId;

    @ExcelProperty(index = 1)
    private String url;

    @ExcelProperty(index = 2)
    private String title;

    @ExcelProperty(index = 3)
    private String relationStaff;

    @ExcelProperty(index = 4)
    private String staffInstitution;

    @ExcelProperty(index = 5)
    private String staffPhone;

    @ExcelProperty(index = 6)
    private String origin;

    @ExcelProperty(index = 7)
    private String introduction;

    @ExcelProperty(index = 8)
    private String indicator;

    @ExcelProperty(index = 9)
    private String application;

    @ExcelProperty(index = 10)
    private String domain;
}
