package com.tgfc.tw.entity.model.response;

import com.tgfc.tw.entity.model.po.Store;

public class StoreAddResponseV2_1 {
    private int id;
    private String storeName;

    public StoreAddResponseV2_1() {

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

    public static StoreAddResponseV2_1 valueOf(Store store) {
        StoreAddResponseV2_1 response = new StoreAddResponseV2_1();
        response.setId(store.getId());
        response.setStoreName(store.getName());
        return response;
    }
}
