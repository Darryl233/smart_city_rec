package com.smartcity.service.rs.entity.HNSW;

import lombok.Data;

@Data
public class Table {
    private short size;
    private byte flag;
    private String label;

    public Table(String label, short size) {
        this.size = size;
        this.label = label;
    }
}

