package com.tgfc.tw.entity.model.response;

import java.util.List;
import java.util.Map;

public class StoreGetOrderDetailResponseV2_1 {
    private int id;
    private String storeName;
    private String storeTel;
    private String storeAddress;
    private String remark;
    private List<TagResponse> tag;
    private List<StorePictureResponse> pictures;

    public StoreGetOrderDetailResponseV2_1() {
    }

    public static StoreGetOrderDetailResponseV2_1 valueOf(Map<String,Object> store){
        StoreGetOrderDetailResponseV2_1 res = new StoreGetOrderDetailResponseV2_1();
        res.setId((int) store.get("sid"));
        res.setStoreName(String.valueOf(store.get("storeName")));
        res.setStoreTel(String.valueOf(store.get("storeTel")));
        res.setStoreAddress(String.valueOf(store.get("storeAddress")));
        res.setRemark(String.valueOf(store.get("remark")));
        return res;
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
