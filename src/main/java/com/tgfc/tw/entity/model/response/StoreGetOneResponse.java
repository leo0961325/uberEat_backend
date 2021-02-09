package com.tgfc.tw.entity.model.response;

import com.tgfc.tw.entity.model.po.Store;

import java.util.List;
import java.util.stream.Collectors;

public class StoreGetOneResponse {

    private int id;
    private String storeName;
    private String storeTel;
    private String storeAddress;
    private String remark;
    private List<TagResponse> tag;
    private List<StorePictureResponse> pictures;

    public StoreGetOneResponse(){

    }

    public StoreGetOneResponse(int id, String storeName, String storeTel, String storeAddress, String remark, List<TagResponse> tag, List<StorePictureResponse> pictures) {
        this.id = id;
        this.storeName = storeName;
        this.storeTel = storeTel;
        this.storeAddress = storeAddress;
        this.remark = remark;
        this.tag = tag;
        this.pictures = pictures;
    }

    public StoreGetOneResponse(Store store) {
        this.id = store.getId();
        this.storeName = store.getName();
        this.storeTel = store.getTel();
        this.storeAddress = store.getAddress();
        this.remark = store.getRemark();
        this.pictures = store.getStorePictureList().stream().map(sp -> new StorePictureResponse(sp)).collect(Collectors.toList());
        this.tag = store.getTagList().stream().map(TagResponse::new).collect(Collectors.toList());
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
