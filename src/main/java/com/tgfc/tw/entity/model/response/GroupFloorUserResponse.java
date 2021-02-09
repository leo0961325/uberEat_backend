package com.tgfc.tw.entity.model.response;

import com.tgfc.tw.entity.model.po.User;

public class GroupFloorUserResponse {

    private int id;
    private String name;
    private String englishName;
    private int floorId;

    public GroupFloorUserResponse(User user) {
        this.id=user.getId();
        this.name=user.getName();
        this.englishName=user.getEnglishName();
        this.floorId=user.getFloor().getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }
}
