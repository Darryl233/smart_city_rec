package com.smartcity.resources_crud.entity.achievement;

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
 * 技术成果
 * </p>
 *
 * @author testjava
 * @since 2020-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SmartcityAchievement对象", description="技术成果")
public class SmartcityAchievement extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "成果ID")
//    @TableId(value = "id", type = IdType.ID_WORKER_STR)
//    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "完成人")
    private String author;

    @ApiModelProperty(value = "完成单位")
    private String mechanism;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "项目年度编号")
    private String number;

    @ApiModelProperty(value = "成果公布年份")
    private Integer year;

    @ApiModelProperty(value = "成果链接")
    private String url;

    @ApiModelProperty(value = "领域")
    private String domain;

    @ApiModelProperty(value = "查看成果价格，设置为0则可免费观看")
    private BigDecimal price;

    @ApiModelProperty(value = "乐观锁")
    @Version
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

    @ApiModelProperty(value = "成果原件")
    private String file;

//    @ApiModelProperty(value = "kg_id")
//    private String kgId;
}
