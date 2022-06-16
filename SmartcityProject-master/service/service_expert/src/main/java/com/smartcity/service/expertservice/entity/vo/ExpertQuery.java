package com.smartcity.service.expertservice.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "专家查询对象", description = "专家查询对象封装")
@Data
public class ExpertQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "专家名称,模糊查询")
    private String name;

    @ApiModelProperty(value = "单位名称，模糊查询")
    private String institution;

    @ApiModelProperty(value = "简介内容，模糊查询")
    private String intro;

    @ApiModelProperty(value = "创建时间的左区间", example = "2019-12-01 10:10:10")
    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "创建时间的有区间", example = "2019-12-01 10:10:10")
    private String end;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

}
