package com.smartcity.service.rs.entity.HNSW;

import java.util.*;

public class HNSWNode {
    private Table table;
    private double[] data;
    private LinkList linkList;  //第1-n层的neighbirs
    private HashSet<String> neighbors;  //第0层neighbors

    public HNSWNode(String label, double[] data) {
        this.table = new Table(label, (short) data.length);
        this.data = data;
        this.linkList = new LinkList();
        this.neighbors = new HashSet<>();
    }

    public ArrayList<String> getNeighbors(int layer) {
        if (layer == 0 && !neighbors.isEmpty())
            return new ArrayList<String>(neighbors);
        else if (layer == 0)
            return new ArrayList<String>();

        HashMap<String, Table> neighbors = linkList.getTable(layer);
        ArrayList<String> res = new ArrayList<>(neighbors.size());
        for (Table neighbor: neighbors.values()) {
            res.add(neighbor.getLabel());
        }
        return res;
    }

    public void addConnection(HNSWNode node, int layer) {
        if (layer < 0) {
            System.out.println("add connection layer error");
            return;
        }
        if (layer == 0) {
            neighbors.add(node.getLabel());
        }
        else {
            linkList.addConnection(node, layer);
        }
    }

    public void delConnection(String node, int layer) {
        if (layer < 0) {
            System.out.println("del connection layer error");
            return;
        }
        if (layer == 0) {
            neighbors.remove(node);
        }
        else {
            linkList.delConnection(node, layer);
        }
    }

    public void setTable(int layer) {
        this.linkList.setLayer(layer);
    }

    public String getLabel() {
        return table.getLabel();
    }

    public double[] getData() {
        return this.data;
    }

    public Table getTable() {
        return this.table;
    }

    public int connectionNum(int layer) {
        if (layer == 0)
            return neighbors.size();
        else
            return linkList.connectionNum(layer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HNSWNode hnswNode = (HNSWNode) o;
        return Objects.equals(table.getLabel(), hnswNode.table.getLabel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(table.getLabel());
    }

    @Override
    public String toString() {
        return "<label: " + String.valueOf(getLabel()) + ", vec: " + Arrays.toString(this.data) + ">";
    }
}
