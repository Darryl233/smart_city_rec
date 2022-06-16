package com.smartcity.service.relationservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class RelationData {
    @ExcelProperty(index = 0)
    private String relationId;

    @ExcelProperty(index = 1)
    private String headId;

    @ExcelProperty(index = 2)
    private String tailId;
}
