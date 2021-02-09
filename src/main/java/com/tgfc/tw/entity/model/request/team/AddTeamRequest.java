package com.tgfc.tw.entity.model.request.team;


import javax.validation.constraints.NotBlank;
import java.util.List;

public class AddTeamRequest {
    @NotBlank(message = "名稱不能空白")
    private String name;
    private List<Integer> userList;

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
