package com.smartcity.service.institutionservice.entity;

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
 * 单位
 * </p>
 *
 * @author testjava
 * @since 2020-11-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SmartcityInstitution对象", description="单位")
public class SmartcityInstitution implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "单位ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "单位名字")
    private String name;

    @ApiModelProperty(value = "类别")
    private String classification;

    @ApiModelProperty(value = "办公地址")
    private String orgaddress;

    @ApiModelProperty(value = "注册地址")
    private String registeaddress;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "经营范围")
    private String businessscope;

    @ApiModelProperty(value = "擅长领域")
    private String domain;

    @ApiModelProperty(value = "单位成立时间")
    private Date createtime;

    @ApiModelProperty(value = "行业")
    private String industry;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "注册资金")
    private String registeredcapital;

    @ApiModelProperty(value = "经营状态")
    private String registerstatus;

    @ApiModelProperty(value = "是否为研究机构, 默认0（否）")
    private Boolean research;

    @ApiModelProperty(value = "单位头像")
    private String avatar;

    @ApiModelProperty(value = "发码平台code")
    private String code;

    @ApiModelProperty(value = "审核状态0/1，默认0(待审核)")
    private Boolean status;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Boolean isDeleted;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "字段创建时间")
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "字段更新时间")
    private Date gmtModified;

    @Version
    @ApiModelProperty(value = "乐观锁")
    private Integer version;

    @ApiModelProperty(value = "kg_id")
    private String kgId;
}
