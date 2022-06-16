package com.smartcity.service.domainservice.entity.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirstDomain {

    private String id;

    private String title;

    private List<SecondDomain> children = new ArrayList<>();
}
