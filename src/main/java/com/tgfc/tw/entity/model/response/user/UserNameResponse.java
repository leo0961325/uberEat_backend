package com.tgfc.tw.entity.model.response.user;

import com.tgfc.tw.entity.model.po.User;

public class UserNameResponse {
    private int id;
    private int floorId;
    private String name;
    private String englishName;

    public UserNameResponse(){

    }

    public UserNameResponse(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.englishName = user.getEnglishName();
        this.floorId = user.getFloor().getId();
    }

    public UserNameResponse(int id, int floorId, String name, String englishName) {
        this.id = id;
        this.floorId = floorId;
        this.name = name;
        this.englishName = englishName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
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
}
