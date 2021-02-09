package com.tgfc.tw.entity.model.response;

import com.tgfc.tw.entity.model.po.Store;

public class StoreNameResponse {
    private int id;
    private String storeName;

    public StoreNameResponse(Store store){
        this(store.getId(), store.getName());
    }

    public StoreNameResponse(int id, String storeName) {
        this.id = id;
        this.storeName = storeName;
    }

    public StoreNameResponse(){

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
}
