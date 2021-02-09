package com.tgfc.tw.entity.model.response;

import com.tgfc.tw.entity.model.response.team.TeamResponse;

import java.util.List;

public class InitOrderResponseV2_1 {
    private List<TeamResponse> teams;
    private List<StoreNameResponse> stores;

    public List<TeamResponse> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamResponse> teams) {
        this.teams = teams;
    }

    public List<StoreNameResponse> getStores() {
        return stores;
    }

    public void setStores(List<StoreNameResponse> stores) {
        this.stores = stores;
    }
}
