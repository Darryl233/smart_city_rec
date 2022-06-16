package com.smartcity.service.rs.entity.HNSW;

import java.util.ArrayList;

public interface IndexService {
    boolean addItem(HNSWNode newNode, int type);

    boolean delItem(HNSWNode newNode, int type);

    ArrayList<HNSWNode> search(double[] data, int type, int topk);


}
