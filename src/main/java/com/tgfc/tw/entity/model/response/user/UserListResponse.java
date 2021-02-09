package com.tgfc.tw.entity.model.response.user;

import com.tgfc.tw.entity.model.po.User;

public class UserListResponse {
    private int id;
    private String username;
    private String name;
    private String englishName;
    private String floorName;

    public UserListResponse(){

    }

    public UserListResponse(User user) {
        setId(user.getId());
        setUsername(user.getUsername());
        setName(user.getName());
        setEnglishName(user.getEnglishName());
        setFloorName(user.getFloor().getName());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }
}
