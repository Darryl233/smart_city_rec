package com.smartcity.service.kgservice.entity;

import io.swagger.annotations.ApiModelProperty;

/**
 * @authorE Zilig Guan
 * @authorC 关哲林
 * @date 2021/12/16 14:58
 **/
public class KGTitleEntity extends KGEntity{
    /**
     * 成果、案例、论文、专利、需求、解决方案 的标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    public KGTitleEntity(String kgId, String title) {
        super(kgId);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
