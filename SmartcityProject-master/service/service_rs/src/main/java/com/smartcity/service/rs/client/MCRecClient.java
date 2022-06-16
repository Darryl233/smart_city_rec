package com.smartcity.service.rs.client;

import java.util.ArrayList;

public interface MCRecClient {
    ArrayList<String> getRecommendation(String user, ArrayList<String> itemList, Integer type);
}
