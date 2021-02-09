package com.tgfc.tw.entity.model.request.store;
import com.tgfc.tw.entity.model.po.Tag;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class StoreRequest {

    private String storeName ;
    private String storeTel ;
    private String storeAddress ;
    private String remark;
    private List<Integer> picList;
    private List<Integer> tagList;


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @NotEmpty(message = "名稱不得為空")
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @NotEmpty(message = "電話不得為空")
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

    public List<Integer> getPicList() {
        return picList;
    }

    public void setPicList(List<Integer> picList) {
        this.picList = picList;
    }

    public List<Integer> getTagList() {
        return tagList;
    }

    public void setTagList(List<Integer> tagList) {
        this.tagList = tagList;
    }
}
