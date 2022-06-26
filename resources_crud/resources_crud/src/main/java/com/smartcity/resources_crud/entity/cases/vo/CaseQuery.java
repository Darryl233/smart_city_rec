package com.smartcity.resources_crud.entity.cases.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "案例查询对象", description = "案例查询对象封装")
@Data
public class CaseQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "案例名称,模糊查询")
    private String title;

    @ApiModelProperty(value = "联系单位，模糊查询")
    private String staffInstitution;

    @ApiModelProperty(value = "简介，模糊查询")
    private String introduction;

    @ApiModelProperty(value = "来源，模糊查询")
    private String origin;

    @ApiModelProperty(value = "特点和指标，模糊查询")
    private String indicator;

    @ApiModelProperty(value = "应用和效益，模糊查询")
    private String application;

    @ApiModelProperty(value = "价格，左区间")
    private String price_left;

    @ApiModelProperty(value = "价格，右区间")
    private String price_right;
}
