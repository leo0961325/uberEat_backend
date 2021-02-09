package com.tgfc.tw.entity.model.response.user;

import com.tgfc.tw.entity.model.po.User;

import java.util.Map;

public class UserNameOnlyResponse {
    private int id;
    private String username;
    private String name;
    private String englishName;

    public static UserNameOnlyResponse valueOf(Map<String,Object> user){
        UserNameOnlyResponse res = new UserNameOnlyResponse();
        res.setId((int) user.get("id"));
        res.setUsername((String) user.get("userName"));
        res.setName((String) user.get("name"));
        res.setEnglishName((String) user.get("englishName"));
        return res;
    }

    public static UserNameOnlyResponse value(User user){
        UserNameOnlyResponse res = new UserNameOnlyResponse();
        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setEnglishName(user.getEnglishName());
        res.setName(user.getName());
        return res;
    }

    public UserNameOnlyResponse(){}

    public UserNameOnlyResponse(int id,String userName,String name,String englishName){
        this.id = id;
        this.username = userName;
        this.name = name;
        this.englishName = englishName;
    }

    public UserNameOnlyResponse(User user) {
        setId(user.getId());
        setUsername(user.getUsername());
        setName(user.getName());
        setEnglishName(user.getEnglishName());
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
}
