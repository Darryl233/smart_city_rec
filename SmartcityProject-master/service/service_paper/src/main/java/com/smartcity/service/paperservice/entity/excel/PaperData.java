package com.smartcity.service.paperservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class PaperData {

    @ExcelProperty(index = 0)
    private String kgId;

    @ExcelProperty(index = 1)
    private String title;

    @ExcelProperty(index = 2)
    private String author;

    @ExcelProperty(index = 3)
    private String mechanism;

    @ExcelProperty(index = 4)
    private String summary;

    @ExcelProperty(index = 5)
    private String keywords;

    @ExcelProperty(index = 6)
    private String classification;

    @ExcelProperty(index = 7)
    private String time;

    @ExcelProperty(index = 8)
    private Long cited;

    @ExcelProperty(index = 9)
    private Long download;

    @ExcelProperty(index = 10)
    private String url;

    @ExcelProperty(index = 11)
    private String domain;

}
