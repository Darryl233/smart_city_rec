package com.smartcity.service.expertservice.entity;

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
 * 专家
 * </p>
 *
 * @author testjava
 * @since 2020-11-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SmartcityExpert对象", description="专家")
public class SmartcityExpert implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "专家ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "专家姓名")
    private String name;

    @ApiModelProperty(value = "专家简介")
    private String intro;

    @ApiModelProperty(value = "职位")
    private String career;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "擅长领域")
    private String domain;

    @ApiModelProperty(value = "传真")
    private String fax;

    @ApiModelProperty(value = "单位")
    private String institution;

    @ApiModelProperty(value = "专家头像")
    private String avatar;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "发码平台code")
    private String code;

    @ApiModelProperty(value = "审核状态0/1，默认0(待审核)")
    private Boolean status;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @ApiModelProperty(value = "乐观锁")
    @Version
    private Integer version;

    @ApiModelProperty(value = "KG_id")
    private String kgId;

    @ApiModelProperty(value = "所在城市")
    private String city;
}
