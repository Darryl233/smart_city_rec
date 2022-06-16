package com.smartcity.service.ucenter.entity;

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
 * 用户浏览记录
 * </p>
 *
 * @author testjava
 * @since 2020-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UserHistory对象", description="用户浏览记录")
public class UserHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "浏览记录ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "登录表中的id")
    private String logId;

    @ApiModelProperty(value = "浏览的资源类型")
    private Integer resourceType;

    @ApiModelProperty(value = "浏览的资源kg_id")
    private String kgId;

    @Version
    @ApiModelProperty(value = "乐观锁")
    private Long version;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Boolean isDeleted;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date gmtModified;


}
