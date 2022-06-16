package com.smartcity.service.solutionservice.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "解决方案查询对象", description = "解决方案对象封装")
@Data
public class SolutionQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "解决方案标题")
    private String title;

    @ApiModelProperty(value = "对应的项目编号")
    private String requirementNumber;

    @ApiModelProperty(value = "中标供应商名称")
    private String orgName;

    @ApiModelProperty(value = "关键词")
    private String keywords;

    @ApiModelProperty(value = "价格，左区间")
    private String price_left;

    @ApiModelProperty(value = "价格，右区间")
    private String price_right;
}
