package com.tgfc.tw.entity.model.response;

import com.tgfc.tw.entity.model.response.user.UserNameResponse;

import java.util.List;

public class InitGroupUserResponse {
    private int groupId;
    private List<UserNameResponse> users;
    private List<FloorResponse> floors;
    private List<StoreResponse> stores;

    public InitGroupUserResponse(){

    }

    public InitGroupUserResponse(int groupId, List<UserNameResponse> users, List<FloorResponse> floors, List<StoreResponse> stores){
        this.groupId = groupId;
        this.users = users;
        this.floors = floors;
        this.stores = stores;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public List<UserNameResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserNameResponse> users) {
        this.users = users;
    }

    public List<FloorResponse> getFloors() {
        return floors;
    }

    public void setFloors(List<FloorResponse> floors) {
        this.floors = floors;
    }

    public List<StoreResponse> getStores() {
        return stores;
    }

    public void setStores(List<StoreResponse> stores) {
        this.stores = stores;
    }
}
