package com.tgfc.tw.entity.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tgfc.tw.entity.model.po.Team;

import java.util.Map;

public class TeamResponse {
    private int id;
    private String name;

    public static TeamResponse valueOf(Map<String,Object> team){
        TeamResponse res = new TeamResponse();
        res.setId((int) team.get("id"));
        res.setName((String) team.get("name"));
        return res;
    }

    public TeamResponse() {
    }

    public TeamResponse(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public TeamResponse(Team team) {
        if(!team.isDeleted()){
            this.id = team.getId();
            this.name = team.getName();
        }
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
}
