package com.tgfc.tw.entity.model.response.orderHistory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgfc.tw.entity.model.po.Store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderHistoryStoreResponseV2_1 {
    private int id;
    private String storeName;
    private String storeTel;
    private String remark;
    private double rank;
    private int totalUsers;
    private Map<Integer, String> tagList = new HashMap<>();

    @JsonProperty("storePicture")
    List<OrderHistoryStorePicResponse> orderHistoryStorePicResponses;

    public OrderHistoryStoreResponseV2_1() {
    }

    public OrderHistoryStoreResponseV2_1(int id, String storeName, String storeTel, String remark, int totalUsers, double rank, Map<Integer, String> tagList, List<OrderHistoryStorePicResponse> orderHistoryStorePicResponses) {
        this.id = id;
        this.storeName = storeName;
        this.storeTel = storeTel;
        this.remark = remark;
        this.tagList = tagList;
        this.totalUsers = totalUsers;
        this.rank = rank;
        this.orderHistoryStorePicResponses = orderHistoryStorePicResponses;
    }

    public OrderHistoryStoreResponseV2_1(Store store){
        this.id = store.getId();
        this.storeName = store.getName();
        this.storeTel = store.getTel();
        this.remark = store.getRemark();
        store.getTagList().stream().forEach(r -> this.tagList.put(r.getId(), r.getName()));
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public double getRank() {
        return rank;
    }
    public void setRank(double rank) {
        this.rank = rank;
    }
    public int getTotalUsers() {
        return totalUsers;
    }
    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }
    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    public String getStoreTel() {
        return storeTel;
    }
    public void setStoreTel(String storeTel) {
        this.storeTel = storeTel;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Map<Integer, String> getTagList() { return tagList; }
    public void setTagList(Map<Integer, String> tagList) { this.tagList = tagList; }

    public List<OrderHistoryStorePicResponse> getOrderHistoryStorePicResponses() {
        return orderHistoryStorePicResponses;
    }
    public void setOrderHistoryStorePicResponses(List<OrderHistoryStorePicResponse> orderHistoryStorePicResponses) {
        this.orderHistoryStorePicResponses = orderHistoryStorePicResponses;
    }

}
