package com.smartcity.service.caseservice.entity;

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
 * 案例
 * </p>
 *
 * @author testjava
 * @since 2020-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SmartcityCase对象", description="案例")
public class SmartcityCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "案例ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "案例标题")
    private String title;

    @ApiModelProperty(value = "案例联系人")
    private String relationStaff;

    @ApiModelProperty(value = "联系人单位")
    private String staffInstitution;

    @ApiModelProperty(value = "联系人联系方式")
    private String staffPhone;

    @ApiModelProperty(value = "来源")
    private String origin;

    @ApiModelProperty(value = "简介")
    private String introduction;

    @ApiModelProperty(value = "特点和指标")
    private String indicator;

    @ApiModelProperty(value = "应用案例和效益")
    private String application;

    @ApiModelProperty(value = "领域")
    private String domain;

    @ApiModelProperty(value = "url")
    private String url;

    @ApiModelProperty(value = "查看案例价格，设置为0则可免费观看")
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

    @ApiModelProperty(value = "案例原件")
    private String file;

    @ApiModelProperty(value = "知识图谱id")
    private String kgId;


}
