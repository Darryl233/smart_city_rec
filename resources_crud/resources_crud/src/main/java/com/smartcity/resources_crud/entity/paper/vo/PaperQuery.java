package com.smartcity.resources_crud.entity.paper.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "论文查询对象", description = "论文查询对象封装")
@Data
public class PaperQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "论文标题,模糊查询")
    private String title;

    @ApiModelProperty(value = "论文作者，模糊查询")
    private String author;

    @ApiModelProperty(value = "摘要内容，模糊查询")
    private String summary;

    @ApiModelProperty(value = "关键词，模糊查询")
    private String keyword;

    @ApiModelProperty(value = "论文价格，左区间")
    private String price_left;

    @ApiModelProperty(value = "论文价格，右区间")
    private String price_right;

    @ApiModelProperty(value = "发表时间的左区间", example = "2019-12-01 10:10:10")
    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "发表时间的右区间", example = "2019-12-01 10:10:10")
    private String end;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

}

