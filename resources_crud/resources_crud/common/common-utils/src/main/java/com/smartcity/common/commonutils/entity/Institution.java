package com.smartcity.common.commonutils.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Institution {

    private String id;

    private String name;

    private String classification;

    private String orgaddress;

    private String registeaddress;

    private String province;

    private String city;

    private String businessscope;

    private String domain;

    private Date createtime;

    private String industry;

    private String phone;

    private String registeredcapital;

    private String registerstatus;

    private Boolean research;

    private String avatar;

    private String code;

    private Boolean status;
}
