package com.smartcity.resources_crud.entity.requirement.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "项目需求查询对象", description = "项目需求对象封装")
@Data
public class RequirementQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "项目需求名称,模糊查询")
    private String title;

    @ApiModelProperty(value = "采购单位，模糊查询")
    private String purchaseInstitution;

    @ApiModelProperty(value = "采购内容描述，模糊查询")
    private String contentDescription;

    @ApiModelProperty(value = "价格，左区间")
    private String price_left;

    @ApiModelProperty(value = "价格，右区间")
    private String price_right;

    @ApiModelProperty(value = "公示时间的左区间", example = "2019-12-01 10:10:10")
    private String announceTime_begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "公示时间的右区间", example = "2019-12-01 10:10:10")
    private String announceTime_end;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "开标时间的左区间", example = "2019-12-01 10:10:10")
    private String openTime_begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "开标时间的右区间", example = "2019-12-01 10:10:10")
    private String openTime_end;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

}
