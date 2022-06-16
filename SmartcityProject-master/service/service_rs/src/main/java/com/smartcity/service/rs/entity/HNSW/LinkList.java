package com.smartcity.service.rs.entity.HNSW;

import java.util.ArrayList;
import java.util.HashMap;

public class LinkList {
    private ArrayList<HashMap<String, Table>> tables;

    public LinkList() {
        tables = new ArrayList<>(5);
    }

    public void addConnection(HNSWNode node, int layer) {
        int curLayer = layer;
        while (curLayer > tables.size()) {
            tables.add(new HashMap<>());
            curLayer--;
        }
        this.getTable(layer).put(node.getLabel(), node.getTable());
    }

    public void setLayer(int layer) {
        while (tables.size() < layer) {
            tables.add(new HashMap<>());
        }
    }

    public ArrayList<HashMap<String, Table>> getTables() {
        return this.tables;
    }

    public HashMap<String, Table> getTable(int layer) {
        if (layer > tables.size()) {
            throw new RuntimeException("exceed max layer");
        }
        return tables.get(layer - 1);
    }

    public int connectionNum(int layer) {
        return tables.get(layer - 1).size();
    }

    public void delConnection(String node, int layer) {
        HashMap<String, Table> datas = tables.get(layer - 1);
        datas.remove(node);
    }
}
