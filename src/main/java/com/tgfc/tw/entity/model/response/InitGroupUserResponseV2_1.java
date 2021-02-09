package com.tgfc.tw.entity.model.response;

import com.tgfc.tw.entity.model.response.team.TeamResponse;
import com.tgfc.tw.entity.model.response.user.UserNameResponse;
import com.tgfc.tw.entity.model.response.user.UserNameResponseV2_1;

import java.util.List;

public class InitGroupUserResponseV2_1 {
    private int groupId;
    private List<UserNameResponseV2_1> users;
    private List<TeamResponse> teams;
    private List<StoreResponse> stores;

    public InitGroupUserResponseV2_1(){

    }

    public InitGroupUserResponseV2_1(int groupId, List<UserNameResponseV2_1> users, List<TeamResponse> teams, List<StoreResponse> stores){
        this.groupId = groupId;
        this.users = users;
        this.teams = teams;
        this.stores = stores;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public List<UserNameResponseV2_1> getUsers() {
        return users;
    }

    public void setUsers(List<UserNameResponseV2_1> users) {
        this.users = users;
    }

    public List<TeamResponse> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamResponse> teams) {
        this.teams = teams;
    }

    public List<StoreResponse> getStores() {
        return stores;
    }

    public void setStores(List<StoreResponse> stores) {
        this.stores = stores;
    }
}
