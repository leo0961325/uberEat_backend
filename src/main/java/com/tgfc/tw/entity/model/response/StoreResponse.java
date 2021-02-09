package com.tgfc.tw.entity.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tgfc.tw.entity.model.po.Store;
import com.tgfc.tw.entity.model.po.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreResponse {
    private int id;
    private String storeName;
    @JsonIgnore
    private int groupId;
    private Map<Integer, String> tagList = new HashMap<>();

    public StoreResponse(){}

    public StoreResponse(Store store){
        this(store.getId(), store.getName(), store.getTagList());
    }

    public StoreResponse(int id,String storeName) {
        this.id = id;
        this.storeName = storeName;
    }

    public StoreResponse(int id,String storeName,int groupId) {
        this.id = id;
        this.storeName = storeName;
        this.groupId = groupId;
    }

    public StoreResponse(int id, String storeName, Map<Integer, String> tagList) {
        this.id = id;
        this.storeName = storeName;
        this.tagList = tagList;
    }

    public StoreResponse(int id, String storeName, List<Tag> tagList) {
        this.id = id;
        this.storeName = storeName;
        tagList.forEach(r->this.tagList.put(r.getId(), r.getName()));
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

    public Map<Integer, String> getTagList() {
        return tagList;
    }

    public void setTagList(Map<Integer, String> tagList) {
        this.tagList = tagList;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
