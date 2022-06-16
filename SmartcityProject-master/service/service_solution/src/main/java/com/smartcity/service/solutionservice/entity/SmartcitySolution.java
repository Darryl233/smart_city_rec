package com.smartcity.service.solutionservice.entity;

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
 * 解决方案
 * </p>
 *
 * @author testjava
 * @since 2020-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SmartcitySolution对象", description="解决方案")
public class SmartcitySolution implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "解决方案ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "解决方案标题")
    private String title;

    @ApiModelProperty(value = "对应的项目编号")
    private String requirementNumber;

    @ApiModelProperty(value = "中标供应商名称")
    private String orgName;

    @ApiModelProperty(value = "中标供应商地址")
    private String orgAddress;

    @ApiModelProperty(value = "中标供应商联系人")
    private String purchasePerson;

    @ApiModelProperty(value = "中标供应商联系人电话")
    private String purchasePhone;

    @ApiModelProperty(value = "中标金额")
    private String budget;

    @ApiModelProperty(value = "关键词")
    private String keywords;

    @ApiModelProperty(value = "领域")
    private String domain;

    @ApiModelProperty(value = "url")
    private String url;

    @ApiModelProperty(value = "查看解决方案价格，设置为0则可免费观看")
    private BigDecimal price;

    @Version
    @ApiModelProperty(value = "乐观锁")
    private Long version;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date gmtModified;

    @ApiModelProperty(value = "发码平台")
    private String code;

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "审核状态，默认0没审核")
    private Boolean status;

    @ApiModelProperty(value = "解决方案原件")
    private String file;

    @ApiModelProperty(value = "知识图谱id")
    private String kgId;

    @ApiModelProperty(value = "是否被需求接受")
    private Boolean isAccepted;

    @ApiModelProperty(value = "简介")
    private String intro;
}
