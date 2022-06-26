package com.smartcity.resources_crud.entity.expert.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExpertData {

    @ExcelProperty(index = 0)
    private String kgId;

    @ExcelProperty(index = 1)
    private String name;

    @ExcelProperty(index = 2)
    private String intro;

    @ExcelProperty(index = 3)
    private String career;

    @ExcelProperty(index = 4)
    private String email;

    @ExcelProperty(index = 5)
    private String phone;

    @ExcelProperty(index = 6)
    private String fax;

    @ExcelProperty(index = 7)
    private String institution;

    @ExcelProperty(index = 8)
    private String city;

    @ExcelProperty(index = 9)
    private String domain;
}
