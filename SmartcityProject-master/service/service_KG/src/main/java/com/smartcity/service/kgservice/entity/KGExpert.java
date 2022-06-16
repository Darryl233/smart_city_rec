package com.smartcity.service.kgservice.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@ApiModel(value="KGExpert对象", description="专家")
public class KGExpert extends KGNameEntity{

    public KGExpert(String kgId, String name) {
        super(kgId, name);
    }
}
