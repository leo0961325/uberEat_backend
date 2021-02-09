package com.tgfc.tw.entity.model.request.store;

public class StoreCommentRequest {

    private int storeId;
    private String message;

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
