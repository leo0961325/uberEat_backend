package com.tgfc.tw.entity.model.request.store;

import javax.validation.constraints.NotNull;
import java.util.List;

public class StoreUpdateRequest {

    private int id;
    private String storeName;
    private String storeTel;
    private String storeAddress;
    private String remark;
    private List<Integer> picId;
    private List<Integer> tagId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotNull
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @NotNull
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

    public List<Integer> getPicId() {
        return picId;
    }

    public void setPicId(List<Integer> picId) {
        this.picId = picId;
    }

    public List<Integer> getTagId() {
        return tagId;
    }

    public void setTagId(List<Integer> tagId) {
        this.tagId = tagId;
    }
}
