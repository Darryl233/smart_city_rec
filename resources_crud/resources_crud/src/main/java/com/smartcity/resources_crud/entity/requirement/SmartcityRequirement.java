package com.smartcity.resources_crud.entity.requirement;

import com.baomidou.mybatisplus.annotation.*;
import com.smartcity.resources_crud.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 项目需求
 * </p>
 *
 * @author testjava
 * @since 2020-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SmartcityRequirement对象", description="项目需求")
public class SmartcityRequirement extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "需求ID")
//    @TableId(value = "id", type = IdType.ID_WORKER_STR)
//    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "需求编号")
    private String requirementNumber;

    @ApiModelProperty(value = "采购单位联系人")
    private String purchasePerson;

    @ApiModelProperty(value = "采购单位联系人电话")
    private String purchasePhone;

    @ApiModelProperty(value = "采购单位名称")
    private String purchaseInstitution;

    @ApiModelProperty(value = "采购单位地址")
    private String purchaseOrgAddress;

    @ApiModelProperty(value = "采购单位联系方式")
    private String orgPhone;

    @ApiModelProperty(value = "采购内容描述")
    private String contentDescription;

    @ApiModelProperty(value = "预算")
    private String budget;

    @ApiModelProperty(value = "关键词")
    private String keywords;

    @ApiModelProperty(value = "公示时间")
    private Date announceTime;

    @ApiModelProperty(value = "开标时间")
    private Date openTime;

    @ApiModelProperty(value = "领域")
    private String domain;

    @ApiModelProperty(value = "查看项目需求价格，设置为0则可免费观看")
    private BigDecimal price;

    @Version
    @ApiModelProperty(value = "乐观锁")
    private Long version;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Integer isDeleted;

//    @TableField(fill = FieldFill.INSERT)
//    @ApiModelProperty(value = "创建时间")
//    private Date gmtCreate;
//
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    @ApiModelProperty(value = "更新时间")
//    private Date gmtModified;

    @ApiModelProperty(value = "发码平台")
    private String code;

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "审核状态，默认0没审核")
    private Boolean status;

    @ApiModelProperty(value = "项目需求原件")
    private String file;

//    @ApiModelProperty(value = "知识图谱id")
//    private String kgId;

    @ApiModelProperty(value = "url")
    private String url;

    @ApiModelProperty(value = "投标开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "投标结束时间")
    private Date endTime;
}
