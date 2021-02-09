package com.tgfc.tw.entity.model.response;

import java.util.List;

public class StoreGetOrderDetailResponse {
    private int id;
    private String storeName;
    private String storeTel;
    private String storeAddress;
    private String remark;
    private List<TagResponse> tag;
    private List<StorePictureResponse> pictures;

    public StoreGetOrderDetailResponse() {
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

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<TagResponse> getTag() {
        return tag;
    }

    public void setTag(List<TagResponse> tag) {
        this.tag = tag;
    }

    public List<StorePictureResponse> getPictures() {
        return pictures;
    }

    public void setPictures(List<StorePictureResponse> pictures) {
        this.pictures = pictures;
    }
}
