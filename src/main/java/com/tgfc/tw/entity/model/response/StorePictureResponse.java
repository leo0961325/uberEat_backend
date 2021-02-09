package com.tgfc.tw.entity.model.response;

import com.tgfc.tw.entity.model.po.StorePicture;

import java.util.Objects;

import java.util.Map;

public class StorePictureResponse {
    private int id;
    private String storePictureUrl;

    private String storePicName;

    public static StorePictureResponse valueOf(Map<String,Object> storePic){
        StorePictureResponse res = new StorePictureResponse();
        res.setId((int) storePic.get("id"));
        res.setStorePicName((String) storePic.get("name"));
        res.setStorePictureUrl((String) storePic.get("url"));
        return res;
    }

    public StorePictureResponse() {

    }

    public StorePictureResponse(int id, String storePictureUrl, String storePicName) {
        this.id = id;
        this.storePictureUrl = storePictureUrl;
        this.storePicName = storePicName;
    }

    public StorePictureResponse(Map<String, Object> map) {
        this.id = (int)map.get("id");
        this.storePictureUrl = String.valueOf(map.get("storePictureUrl"));
        this.storePicName = String.valueOf(map.get("storePicName"));
    }

    public StorePictureResponse(StorePicture storePicture) {
        this.id = storePicture.getId();
        this.storePictureUrl = storePicture.getUrl();
        this.storePicName = storePicture.getName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStorePicName() {
        return storePicName;
    }

    public void setStorePicName(String storePicName) {
        this.storePicName = storePicName;
    }

    public String getStorePictureUrl() {
        return storePictureUrl;
    }

    public void setStorePictureUrl(String storePictureUrl) {
        this.storePictureUrl = storePictureUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorePictureResponse that = (StorePictureResponse) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
