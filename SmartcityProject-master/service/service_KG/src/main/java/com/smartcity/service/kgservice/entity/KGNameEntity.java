package com.smartcity.service.kgservice.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @authorE Zilig Guan
 * @authorC 关哲林
 * @date 2021/12/16 14:57
 **/

public abstract class KGNameEntity extends KGEntity{
    /**
     * 专家、单位 的名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    public KGNameEntity(String kgId, String name) {
        super(kgId);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
