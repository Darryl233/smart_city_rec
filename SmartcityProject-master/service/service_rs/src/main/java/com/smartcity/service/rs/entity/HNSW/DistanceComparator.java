package com.smartcity.service.rs.entity.HNSW;

import java.util.ArrayList;
import java.util.Comparator;

public class DistanceComparator implements Comparator<HNSWNode> {
    private final HNSWNode q;
    private ArrayList<double[]> relationEmbeddings;

    public DistanceComparator(HNSWNode q, ArrayList<double[]> relationEmbeddings) {
        this.q = q;
        this.relationEmbeddings = relationEmbeddings;
    }

    @Override
    public int compare(HNSWNode o1, HNSWNode o2) {
        double dis1 = 0;
        double dis2 = 0;

        dis1 = this.getDistance(o1);
        dis2 = this.getDistance(o2);

        if (dis1 > dis2)
            return -1;
        else if (dis1 == dis2)
            return 0;
        return 1;
    }

//    private double getDistance(HNSWNode node) {
//        double res = 0.0;
//        if (this.relationEmbeddings.isEmpty())
//            return res;
//        for (double[] relation: this.relationEmbeddings) {
//            double[] dis = new double[node.getTable().getSize()];
//            for(int i = 0; i < dis.length; i++) {
//                dis[i] = q.getData()[i] + relation[i] - node.getData()[i];
//            }
//            double temp = 0.0;
//            for (int i = 0; i < dis.length; i++) {
//                temp += (dis[i] * dis[i]);
//            }
//            if (temp > res)
//                res = temp;
//            for(int i = 0; i < dis.length; i++) {
//                dis[i] = -q.getData()[i] + relation[i] + node.getData()[i];
//            }
//            for (int i = 0; i < dis.length; i++) {
//                temp += (dis[i] * dis[i]);
//            }
//            if (temp > res)
//                res = temp;
//        }
//        return res;
//
//    }
    private double getDistance(HNSWNode node) {
        double res = 0.0;
        double[] vec1 = node.getData();
        double[] vec2 = this.q.getData();
        for (int i = 0; i < vec1.length; i++) {
            res += Math.pow((vec1[i] - vec2[i]), 2);
        }
        return res;
    }

}
