package com.tgfc.tw.entity.model.response.user;

import com.tgfc.tw.entity.model.response.team.TeamResponse;

import java.util.List;

public class UserAllListResponse {
    private int id;
    private String name;
    private String englishName;
    private List<TeamResponse> team;

    public UserAllListResponse() {
    }

    public UserAllListResponse(int id, String name, String englishName) {
        this.id = id;
        this.name = name;
        this.englishName = englishName;
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

    public List<TeamResponse> getTeam() {
        return team;
    }

    public void setTeam(List<TeamResponse> team) {
        this.team = team;
    }
}
