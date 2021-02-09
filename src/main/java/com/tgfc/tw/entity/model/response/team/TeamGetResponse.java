package com.tgfc.tw.entity.model.response.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgfc.tw.entity.model.po.Team;
import com.tgfc.tw.entity.model.response.user.UserNameOnlyResponse;

import java.util.List;

public class TeamGetResponse {
    private int id;
    private String teamName;
    @JsonProperty("userList")
    private List<UserNameOnlyResponse> userNameOnlyResponseList;

    public static TeamGetResponse valueOf(Team team){
        TeamGetResponse teamGetResponse = new TeamGetResponse();
        teamGetResponse.setTeamName(team.getName());
        teamGetResponse.setId(team.getId());
        return teamGetResponse;
    }

    public TeamGetResponse() {
    }

    public TeamGetResponse(Team team) {
        this.id = team.getId();
        this.teamName = team.getName();
    }

    public TeamGetResponse(int id, String teamName,List<UserNameOnlyResponse> userList) {
        this.id = id;
        this.teamName = teamName;
        this.userNameOnlyResponseList = userList;
    }

    public TeamGetResponse(int id, String teamName) {
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

    public List<UserNameOnlyResponse> getUserNameOnlyResponseList() {
        return userNameOnlyResponseList;
    }

    public void setUserNameOnlyResponseList(List<UserNameOnlyResponse> userNameOnlyResponseList) {
        this.userNameOnlyResponseList = userNameOnlyResponseList;
    }
}
