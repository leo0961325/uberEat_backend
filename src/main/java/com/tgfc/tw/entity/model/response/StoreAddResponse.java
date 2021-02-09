package com.tgfc.tw.entity.model.response;

public class StoreAddResponse {
    private int id;
    private String storeName;

    public StoreAddResponse(){

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
