package com.smartcity.service.achievementservice.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "成果查询对象", description = "封装成果查询对象")
public class AchievementQuery {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "成果标题,模糊查询")
    private String title;

    @ApiModelProperty(value = "完成人，模糊查询")
    private String author;

    @ApiModelProperty(value = "完成单位，模糊查询")
    private String mechanism;

    @ApiModelProperty(value = "摘要内容，模糊查询")
    private String summary;

    @ApiModelProperty(value = "关键词，模糊查询")
    private String keyword;

    @ApiModelProperty(value = "成果价格，左区间")
    private String price_left;

    @ApiModelProperty(value = "成果价格，右区间")
    private String price_right;

    @ApiModelProperty(value = "公布年份的左区间", example = "2019")
    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "公布年份的右区间", example = "2019")
    private String end;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换
}
