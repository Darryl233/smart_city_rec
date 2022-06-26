package com.smartcity.resources_crud.entity.paper;

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
 * 论文
 * </p>
 *
 * @author testjava
 * @since 2020-11-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SmartcityPaper对象", description="论文")
public class SmartcityPaper extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "论文ID")
//    @TableId(value = "id", type = IdType.ID_WORKER_STR)
//    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "作者所属机构")
    private String mechanism;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "分类号")
    private String classification;

    @ApiModelProperty(value = "出版日期")
    private Date pubDate;

    @ApiModelProperty(value = "被引次数")
    private Long cited;

    @ApiModelProperty(value = "下载次数")
    private Long download;

    @ApiModelProperty(value = "文件链接")
    private String url;

    @ApiModelProperty(value = "文件原件")
    private String file;

    @ApiModelProperty(value = "查看论文价格，设置为0则可免费观看")
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

//    @ApiModelProperty(value = "kg_id")
//    private String kgId;

    @ApiModelProperty(value = "领域")
    private String domain;
}
