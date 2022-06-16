package com.smartcity.service.rs.entity.HNSW;

import java.util.ArrayList;

public interface Index {
    boolean addItem(HNSWNode newNode);

    boolean delItem(HNSWNode newNode);

    ArrayList<HNSWNode> search(HNSWNode query, int topk);
}
