package com.smartcity.service.rs.entity;

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
 * 推荐
 * </p>
 *
 * @author testjava
 * @since 2020-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Recommendation对象", description="推荐")
public class Recommendation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "推荐ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String logId;

    @ApiModelProperty(value = "推荐资源类型(1专家，2论文)")
    private Integer type;

    @ApiModelProperty(value = "资源0——ID")
    private String itemId;

    @ApiModelProperty(value = "协同过滤得分")
    private Float score;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date gmtModified;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除1删除，0未删除")
    private Integer idDeleted;

    @Version
    @ApiModelProperty(value = "乐观锁")
    private Long version;


}
