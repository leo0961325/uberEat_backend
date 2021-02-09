package com.tgfc.tw.entity.model.response.user;

import com.tgfc.tw.entity.model.po.Team;
import com.tgfc.tw.entity.model.po.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserNameResponseV2_1 {
    private int id;
    private List<Integer> teamId;
    private String name;
    private String englishName;

    public UserNameResponseV2_1() {

    }

    public UserNameResponseV2_1(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.englishName = user.getEnglishName();
        List<Integer> teamIdList = user.getTeamList().stream().filter(t -> !t.isDeleted())
                .map(t -> t.getId()).collect(Collectors.toList());
        this.teamId = teamIdList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getTeamId() {
        return teamId;
    }

    public void setTeamId(List<Integer> teamId) {
        this.teamId = teamId;
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
