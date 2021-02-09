package com.tgfc.tw.entity.model.response.team;

import com.tgfc.tw.entity.model.po.Team;

public class TeamResponse {
    private int id;
    private String teamName;

    public TeamResponse() {
    }
    public TeamResponse(Team team) {
        this.id = team.getId();
        this.teamName = team.getName();
    }

    public TeamResponse(int id, String teamName) {
        this.id = id;
        this.teamName = teamName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
