package com.smartcity.service.patentservice.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 专利
 * </p>
 *
 * @author testjava
 * @since 2020-11-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SmartcityPatent对象", description="专利")
public class SmartcityPatent implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "专利ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "申请日")
    private Date applicationDate;

    @ApiModelProperty(value = "公开日")
    private Date openDate;

    @ApiModelProperty(value = "申请号")
    private String applicationId;

    @ApiModelProperty(value = "公开号")
    private String openId;

    @ApiModelProperty(value = "申请人")
    private String applicant;

    @ApiModelProperty(value = "共同申请人")
    private String coApplicants;

    @ApiModelProperty(value = "发明人")
    private String inventor;

    @ApiModelProperty(value = "国际申请")
    private String viewCount;

    @ApiModelProperty(value = "进入国家日期")
    private Date enterCountryDate;

    @ApiModelProperty(value = "专利代理机构")
    private String agency;

    @ApiModelProperty(value = "分案原申请号")
    private String originalApplicationId;

    @ApiModelProperty(value = "国省代码")
    private String provinceId;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "主权项")
    private String mainClain;

    @ApiModelProperty(value = "页数")
    private Integer pages;

    @ApiModelProperty(value = "主分类号")
    private String mainClassification;

    @ApiModelProperty(value = "专利分类号")
    private String patentClassification;

    @ApiModelProperty(value = "查看专利价格，设置为0则可免费观看")
    private BigDecimal price;

    @ApiModelProperty(value = "乐观锁")
    @Version
    private Long version;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @ApiModelProperty(value = "发码平台")
    private String code;

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "审核状态，默认0没审核")
    private Boolean status;

    @ApiModelProperty(value = "url")
    private String url;

    @ApiModelProperty(value = "文件原件")
    private String file;

    @ApiModelProperty(value = "领域")
    private String domain;

    @ApiModelProperty(value = "kg_id")
    private String kgId;
}
