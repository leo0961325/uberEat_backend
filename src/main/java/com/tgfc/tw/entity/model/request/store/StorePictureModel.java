package com.tgfc.tw.entity.model.request.store;

import java.util.List;

public class StorePictureModel {
    private int id;
    private int storeId;
    private String storePictureUrl;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStorePictureUrl() {
        return storePictureUrl;
    }

    public void setStorePictureUrl(String storePictureUrl) {
        this.storePictureUrl = storePictureUrl;
    }
}
