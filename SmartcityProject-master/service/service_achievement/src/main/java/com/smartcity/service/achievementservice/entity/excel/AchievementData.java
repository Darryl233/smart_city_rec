package com.smartcity.service.achievementservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class AchievementData {
    @ExcelProperty(index = 0)
    private String kgId;

    @ExcelProperty(index = 1)
    private String reference_type;

    @ExcelProperty(index = 2)
    private String title;

    @ExcelProperty(index = 3)
    private String author;

    @ExcelProperty(index = 4)
    private String mechanism;

    @ExcelProperty(index = 5)
    private Integer year;

    @ExcelProperty(index = 6)
    private String number;

    @ExcelProperty(index = 7)
    private String keywords;

    @ExcelProperty(index = 8)
    private String summary;

    @ExcelProperty(index = 9)
    private String url;

    @ExcelProperty(index = 10)
    private String domain;
}
