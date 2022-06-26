package com.smartcity.resources_crud.entity.patent.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "专利查询对象", description = "专利查询对象封装")
@Data
public class PatentQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "专利标题,模糊查询")
    private String title;

    @ApiModelProperty(value = "专利申请人，模糊查询")
    private String applicant;

    @ApiModelProperty(value = "摘要内容，模糊查询")
    private String summary;

    @ApiModelProperty(value = "论文价格，左区间")
    private String price_left;

    @ApiModelProperty(value = "论文价格，右区间")
    private String price_right;

    @ApiModelProperty(value = "申请日时间的左区间", example = "2019-12-01 10:10:10")
    private String application_begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "申请日时间的右区间", example = "2019-12-01 10:10:10")
    private String application_end;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "公开日时间的左区间", example = "2019-12-01 10:10:10")
    private String open_begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "公开日时间的右区间", example = "2019-12-01 10:10:10")
    private String open_end;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换


}
