package com.smartcity.resources_crud.entity.institution.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "单位查询对象", description = "单位查询对象封装")
@Data
public class InstitutionQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "单位名称,模糊查询")
    private String name;

    @ApiModelProperty(value = "单位行业，模糊查询")
    private String industry;

    @ApiModelProperty(value = "类别，模糊查询")
    private String classification;

    @ApiModelProperty(value = "注册成立时间的左区间", example = "2019-12-01")
    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "注册成立时间的有区间", example = "2019-12-01")
    private String end;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换
}
