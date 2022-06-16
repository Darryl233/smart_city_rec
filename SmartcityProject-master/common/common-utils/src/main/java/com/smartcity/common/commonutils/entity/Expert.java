package com.smartcity.common.commonutils.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
public class Expert implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String intro;

    private String career;

    private String email;

    private String phone;

    private String domain;

    private String fax;

    private String institution;

    private String avatar;

    private String code;

    private Boolean status;
}
