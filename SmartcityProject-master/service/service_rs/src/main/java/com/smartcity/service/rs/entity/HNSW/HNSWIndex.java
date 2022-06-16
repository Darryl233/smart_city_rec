package com.smartcity.service.rs.entity.HNSW;

import java.util.*;

public class HNSWIndex implements Index{
    private HashMap<String, HNSWNode> level0;
    private ArrayList<HashMap<String, HNSWNode>> layers;
    private final int DIMENSION;
    private int maxConnect;
    private int cef = 100;
    private int sef = 50;
    private double connectLowLimitRatio = 0.33;
    private int curElementCount;
    private ArrayList<double[]> relationEmbeddings;

    public HNSWIndex(int maxConnect, int dimension) {
        this.maxConnect = maxConnect;
        this.DIMENSION = dimension;
        this.level0 = new HashMap<>();
        this.layers = new ArrayList<>();
        this.layers.add(level0);
    }

    public HNSWIndex(int maxConnect, int dimension, int cef, int sef) {
        this(maxConnect, dimension);
        this.cef = cef;
        this.sef = sef;
    }

    @Override
    public boolean addItem(HNSWNode newNode) {
        // 向量维数检查
        if (newNode.getData().length != this.DIMENSION) {
            System.out.println("vector dimension nor match");
            return false;
        }
        curElementCount += 1;
        level0.put(newNode.getLabel(), newNode);
        int curLayer = getMaxLevel();
        newNode.setTable(curLayer);
        int layerNum = getIndexMaxLayer();
        while (curLayer > layerNum) {
            HashMap<String, HNSWNode> nextLayer = new HashMap<>();
            nextLayer.put(newNode.getLabel(), newNode);
            layers.add(nextLayer);
            // System.out.println("curLayer: " + String.valueOf(curLayer));
            curLayer --;
        }
        for (int i = 1; i <= curLayer; i++) {
            layers.get(i).put(newNode.getLabel(), newNode);
        }
        HNSWNode curEp = null;
        double curDistance = 0;
        Random random = new Random();
        layerNum = getIndexMaxLayer();
        int idx;
        if (getLayerData(layerNum).size() > 1)
            idx = random.nextInt(getLayerData(layerNum).size() - 1);
        else
            idx = 0;
        int i = 0;
        for (HNSWNode hnswNode : getLayerData(layerNum).values()) {
            if (i == idx) {
                curEp = hnswNode;
                if (curEp == newNode) {
                    curEp = null;
                    idx++;
                    i++;
                    continue;
                }
                curDistance = getDistance(newNode, curEp);
                break;
            }
            i++;
        }
        // 获取插入层ep
        if (curEp != null) {
            while (layerNum > curLayer) {
                boolean changed = true;
                while (changed) {
                    changed = false;
                    ArrayList<String> curNeighbors = curEp.getNeighbors(curLayer);
                    for (String neighbor : curNeighbors) {
                        double distance = getDistance(newNode, level0.get(neighbor));
                        if (distance < curDistance) {
                            curDistance = distance;
                            curEp = level0.get(neighbor);
                            changed = true;
                        }
                    }
                }
                layerNum--;
            }
            while (curLayer >= 0) {
                HashSet<String> visited = new HashSet<>();
                PriorityQueue<HNSWNode> nearestNodes = searchLayer(curEp, newNode, curLayer, maxConnect, true, visited);
                ArrayList<HNSWNode> temp = new ArrayList<>(nearestNodes);
                temp.sort(new DistanceComparator(newNode, this.relationEmbeddings));
                for (HNSWNode node : nearestNodes) {
                    node.addConnection(newNode, curLayer);
                    if (node.connectionNum(curLayer) > this.maxConnect) {
                        String label = getFurthestNeighbor(node, curLayer);
                        if (level0.get(label).connectionNum(curLayer) > maxConnect * this.connectLowLimitRatio) {
                            level0.get(label).delConnection(node.getLabel(), curLayer);
                            node.delConnection(label, curLayer);
                        }
                    }
                    newNode.addConnection(node, curLayer);
                }
                curEp = temp.get(temp.size()-1);
                curLayer--;
            }
        }

        return true;
    }

    @Override
    public boolean delItem(HNSWNode newNode) {
        return false;
    }

    @Override
    public ArrayList<HNSWNode> search(HNSWNode query, int topk) {
        ArrayList<HNSWNode> res = new ArrayList<>(topk);
        int indexLayerNum = getIndexMaxLayer();
        HNSWNode ep = null;
        // 随机选取ep
        Random random = new Random();
        int epIndex;
        if (getLayerData(indexLayerNum).size() > 1)
            epIndex = random.nextInt(getLayerData(indexLayerNum).size() - 1);
        else
            epIndex = 0;
        int i = 0;
        for (HNSWNode node : getLayerData(indexLayerNum).values()) {
            ep = node;
            if (i == epIndex)
                break;
            i++;
        }
        if (ep == null) {
            System.out.println("enter point null");
            return res;
        }
        //System.out.println("enter point: " + ep.toString());
        HashSet<String> visited = new HashSet<>();
        PriorityQueue<HNSWNode> candidates;
        PriorityQueue<HNSWNode> layerCandidates = new PriorityQueue<>(this.sef, new DistanceComparator(query, this.relationEmbeddings));
        int curLayer = indexLayerNum;
        candidates = searchLayer(ep, query, curLayer, this.sef, false, visited);
        //System.out.println(candidates);
        while (curLayer > 0) {
            mergeCandidates(candidates, layerCandidates, this.sef);
            layerCandidates.clear();
            // todo: 并发优化
            for (HNSWNode candidate: candidates) {
                PriorityQueue<HNSWNode> tmp = searchLayer(candidate, query, curLayer - 1, this.sef, false, visited);
                //System.out.println(tmp.toString());
                mergeCandidates(layerCandidates, tmp, this.sef);
            }
            //System.out.println("layer candidates: " + layerCandidates.toString());
            curLayer--;
        }
        mergeCandidates(candidates, layerCandidates, this.sef);
        int resNum = Math.min(candidates.size(), topk);
        while (candidates.size() > resNum){
            candidates.poll();
        }
        while (!candidates.isEmpty()) {
            res.add(candidates.poll());
        }
        Collections.reverse(res);
        return res;
    }


    private int getMaxLevel() {
        double rawLevel = -Math.log(Math.random()) * (1.0 / Math.log(maxConnect));
        return (int) rawLevel;
    }

    public PriorityQueue<HNSWNode> searchLayer(HNSWNode ep, HNSWNode q, int layer, int topk, boolean isConstruct, HashSet<String> visited) {
        PriorityQueue<HNSWNode> candidates, newCandidates;
        int ef;
        if (isConstruct)
            ef = this.cef;
        else
            ef = this.sef;
        candidates = new PriorityQueue<>(ef, new DistanceComparator(q, this.relationEmbeddings));
        newCandidates = new PriorityQueue<>(ef, new DistanceComparator(q, this.relationEmbeddings));

        PriorityQueue<HNSWNode> result = new PriorityQueue<>(topk, new DistanceComparator(q, this.relationEmbeddings));
        visited.add(ep.getLabel());
        newCandidates.add(ep);
        double lowerBound = getDistance(ep, q);
        do {
            //System.out.println(newCandidates);
            mergeCandidates(candidates, newCandidates, ef);
            for (HNSWNode node : candidates) {
                ArrayList<String> neighborLabels = node.getNeighbors(layer);
                ArrayList<HNSWNode> neighbors = new ArrayList<>();
                if (!neighborLabels.isEmpty()) {
                    for (String label : neighborLabels) {
                        neighbors.add(level0.get(label));
                    }
                    for (HNSWNode neighbor : neighbors) {
                        if (!visited.contains(neighbor.getLabel())) {
                            visited.add(neighbor.getLabel());
                            double distance = getDistance(neighbor, q);
                            if (distance < lowerBound || newCandidates.size() < ef) {
                                newCandidates.add(neighbor);
                            }
                            if (newCandidates.size() > ef) {
                                newCandidates.poll();
                            }
                            if (!newCandidates.isEmpty() && newCandidates.size() == ef)
                                lowerBound = getDistance(q, newCandidates.peek());
                        }
                    }
                }
            }
        } while (!candidatesEqual(candidates, newCandidates, topk));
        int resNum = Math.min(topk, candidates.size());
        while (candidates.size() > resNum) {
            candidates.poll();
        }
        while (!candidates.isEmpty()) {
            result.add(candidates.poll());
        }

        return result;
    }

    // todo: 并发优化优化
    //ep: enter point, q: query, ef:动态列表容量
    private PriorityQueue<HNSWNode> searchLayer(HNSWNode ep, HNSWNode q, int layer, int topk, boolean isConstruct) {
        HashSet<String> visited = new HashSet<>();
        return searchLayer(ep, q, layer, topk, isConstruct, visited);
    }

    private boolean candidatesEqual(PriorityQueue<HNSWNode>q1, PriorityQueue<HNSWNode> q2, int topk) {
        ArrayList<HNSWNode> temp1 = new ArrayList<>(q1.size());
        ArrayList<HNSWNode> temp2 = new ArrayList<>(q2.size());
        HNSWNode node1, node2;
        int n;
        if (!q1.isEmpty() && !q2.isEmpty()) {
            if (q1.size() == q2.size()) {
                n = Math.min(q1.size(), topk);
                for (int i = 0; i < n; i++) {
                    node1 = q1.poll();
                    node2 = q2.poll();
                    temp1.add(node1);
                    temp2.add(node2);
                    if (!node1.equals(node2)) {
                        q1.addAll(temp1);
                        q2.addAll(temp2);
                        return false;
                    }
                }
                q1.addAll(temp1);
                q2.addAll(temp2);
                return true;
            }
            q1.addAll(temp1);
            q2.addAll(temp2);
            return false;
        }
        else return q1.isEmpty() && q2.isEmpty();
    }

    private void mergeCandidates(PriorityQueue<HNSWNode> candidates, PriorityQueue<HNSWNode> tmp, int ef) {
        HashSet<String> dedup = new HashSet<>();
        for (HNSWNode node : candidates) {
            dedup.add(node.getLabel());
        }
        for (HNSWNode node : tmp) {
            if (!dedup.contains(node.getLabel())) {
                candidates.add(node);
                dedup.add(node.getLabel());
                if (candidates.size() > ef)
                    candidates.poll();
            }
        }
    }

    private double getDistance(HNSWNode n1, HNSWNode n2) {
        double[] v1 = n1.getData();
        double[] v2 = n2.getData();
        double res = 0;
        for(int i = 0; i < v1.length; i++) {
            res -= v1[i] * v2[i];
        }
        return res;
    }

    public String getFurthestNeighbor(HNSWNode node, int curLayer) {
        ArrayList<String> list = node.getNeighbors(curLayer);
        double maxDis = getDistance(node, level0.get(list.get(0)));
        String maxLabel = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            double dis = getDistance(node, level0.get(list.get(i)));
            if (dis > maxDis) {
                maxLabel = list.get(i);
                maxDis = dis;
            }
        }
        return maxLabel;
    }

    public int getIndexMaxLayer() {
        return layers.size() - 1;
    }

    public HashMap<String, HNSWNode> getLayerData(int layer) {
        if (layer > layers.size() || layer < 0) {
            throw new RuntimeException("invalid layer");
        } else {
            return layers.get(layer);
        }
    }

    public void setMaxConnect(int maxConnect) {
        this.maxConnect = maxConnect;
    }

    public void setCef(int cef) {
        this.cef = cef;
    }

    public void setSef(int sef) {
        this.sef = sef;
    }

    public int getCurElementCount() {
        return curElementCount;
    }

    public void setConnectLowLimitRatio(double connectLowLimitRatio) {
        this.connectLowLimitRatio = connectLowLimitRatio;
    }

    public void setRelationEmbeddings(ArrayList<double[]> relationEmbeddings) {
        this.relationEmbeddings = relationEmbeddings;
    }

    public int getDIMENSION() {
        return this.DIMENSION;
    }
}
