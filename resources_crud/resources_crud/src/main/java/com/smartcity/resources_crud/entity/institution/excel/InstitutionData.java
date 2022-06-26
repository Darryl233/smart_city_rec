package com.smartcity.resources_crud.entity.institution.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class InstitutionData {
    @ExcelProperty(index = 0)
    private String kgId;

    @ExcelProperty(index = 1)
    private String fullname;

    @ExcelProperty(index = 2)
    private String classification;

    @ExcelProperty(index = 3)
    private String orgAddress;

    @ExcelProperty(index = 4)
    private String registeAddress;

    @ExcelProperty(index = 5)
    private String province;

    @ExcelProperty(index = 6)
    private String city;

    @ExcelProperty(index = 7)
    private String businessScope;

    @ExcelProperty(index = 8)
    private String createTime;

    @ExcelProperty(index = 9)
    private String fieldTypeName;

    @ExcelProperty(index = 10)
    private String industryName;

    @ExcelProperty(index = 11)
    private String orgPhone;

    @ExcelProperty(index = 12)
    private String registeredCapital;

    @ExcelProperty(index = 13)
    private String registerStatus;

    @ExcelProperty(index = 14)
    private Boolean research;


}
