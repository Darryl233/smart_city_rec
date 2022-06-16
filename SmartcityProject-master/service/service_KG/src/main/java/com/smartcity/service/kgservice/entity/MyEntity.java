package com.smartcity.service.kgservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyEntity {

    public String kg_id;

    public Map<String, String> propertyValues;
}
