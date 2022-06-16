package com.smartcity.service.ucenter.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "注册对象", description = "注册对象")
public class RegisterVo {

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户类型，1普通用户，2专家，3单位，4城市")
    private Integer type;

    @ApiModelProperty(value = "用户表ID")
    private String userId;
}
