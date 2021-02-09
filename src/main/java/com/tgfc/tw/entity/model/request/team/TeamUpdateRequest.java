package com.tgfc.tw.entity.model.request.team;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class TeamUpdateRequest {
    private int id;
    private String name;
    private List<Integer> userList;

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

    public List<Integer> getUserList() {
        return userList;
    }

    public void setUserList(List<Integer> userList) {
        this.userList = userList;
    }
}
