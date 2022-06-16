package com.smartcity.service.kgservice.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;

/**
 * @authorE Zilig Guan
 * @authorC 关哲林
 * @date 2021/12/16 14:02
 **/
@AllArgsConstructor
public abstract class KGEntity {
    /**
     * 自定义的id，全局唯一，形如 "expert_100"
     */
    @ApiModelProperty(value = "自定义 kg_id")
    private String kgId;

    public String getKgId() {
        return kgId;
    }

    public void setKgId(String kgId) {
        this.kgId = kgId;
    }
}
