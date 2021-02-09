package com.tgfc.tw.entity.model.response;

import com.tgfc.tw.entity.model.po.User;

import java.util.ArrayList;
import java.util.List;

public class GroupTeamUserResponse {

    private int id;
    private String name;
    private String englishName;
    private List<Integer> teamIdList;

    public GroupTeamUserResponse(User user) {
        this.id=user.getId();
        this.name=user.getName();
        this.englishName=user.getEnglishName();

        List<Integer> addIdList = new ArrayList<>();
        user.getTeamList().forEach(r->addIdList.add(r.getId()));
        this.teamIdList= addIdList;
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

    public List<Integer> getTeamIdList() {
        return teamIdList;
    }

    public void setTeamIdList(List<Integer> teamIdList) {
        this.teamIdList = teamIdList;
    }
}
