package com.smartcity.service.kgservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Relation {

    private String relationId;

    private String headId;

    private String tailId;
}
