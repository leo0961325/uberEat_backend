package com.tgfc.tw.entity.model.response.orderHistory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgfc.tw.entity.model.po.Store;
import org.springframework.beans.BeanUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderHistoryStoreResponse {
    private int id;
    private String storeName;
    private String storeTel;
    private String remark;
    private Map<Integer, String> tagList = new HashMap<>();

    @JsonProperty("storePicture")
    List<OrderHistoryStorePicResponse> orderHistoryStorePicResponses;
    /*@JsonProperty("order")
    List<OrderHistoryOrderResponse> orderHistoryOrderResponses;*/

    public OrderHistoryStoreResponse() {

    }

    public OrderHistoryStoreResponse(int id, String storeName, String storeTel, String remark, Map<Integer, String> tagList, List<OrderHistoryStorePicResponse> orderHistoryStorePicResponses) {
        this.id = id;
        this.storeName = storeName;
        this.storeTel = storeTel;
        this.remark = remark;
        this.tagList = tagList;
        this.orderHistoryStorePicResponses = orderHistoryStorePicResponses;
    }

    public OrderHistoryStoreResponse(Store store){
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
    /*public List<OrderHistoryOrderResponse> getOrderHistoryOrderResponses() {
        return orderHistoryOrderResponses;
    }
    public void setOrderHistoryOrderResponses(List<OrderHistoryOrderResponse> orderHistoryOrderResponses) {
        this.orderHistoryOrderResponses = orderHistoryOrderResponses;
    }*/

    public static List<OrderHistoryStoreResponse> valueListOf(List<Store> storeList, List<OrderHistoryUserRespense> histories) {

        List<OrderHistoryStoreResponse> list = new ArrayList<>();
        for(Store store : storeList){
            OrderHistoryStoreResponse newStore = OrderHistoryStoreResponse.valueOf(store, histories);
            list.add(newStore);
        }
        return list;
    }

    private static OrderHistoryStoreResponse valueOf(Store store, List<OrderHistoryUserRespense> histories) {
        OrderHistoryStoreResponse newStore = new OrderHistoryStoreResponse(store);
        newStore.setOrderHistoryStorePicResponses(OrderHistoryStorePicResponse.valueListOf(store.getStorePictureList()));
        //newStore.setOrderHistoryOrderResponses(OrderHistoryOrderResponse.valueListOf(store.getProductList(), histories));
        return newStore;
    }
}
