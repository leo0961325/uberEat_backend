package com.tgfc.tw.entity.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tgfc.tw.entity.model.po.Group;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupItemResponse {

    private int id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp endTime;
    private Map<Integer, String> groupBuyingType;
    private boolean isLocked;
    private boolean isDeleted;
    private List<StoreResponse> storeList;
    private List<Integer> userList;
    private Map<Integer, GroupUserStatusResponse> userStatus;

    public static GroupItemResponse valueOf(Group group) {
        GroupItemResponse item = new GroupItemResponse();

        item.id = group.getId();
        item.name = group.getName();
        item.startTime = group.getStartTime();
        item.endTime = group.getEndTime();
        item.isLocked = group.isLocked();
        item.isDeleted = group.isDeleted();
        item.groupBuyingType = new HashMap<>();
        if(group.getStoreList() != null && group.getStoreList().size() != 0){
            item.storeList = group.getStoreList().stream()
                    .map(r->new StoreResponse(r))
                    .collect(Collectors.toList());
        }
        if (!(group.getUserList() == null || group.getUserList().size() == 0)) {
            item.userList = group.getUserList().stream()
                    .map(r -> r.getId())
                    .collect(Collectors.toList());
        }
        return item;
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

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Map<Integer, String> getGroupBuyingType() {
        return groupBuyingType;
    }

    public void setGroupBuyingType(Map<Integer, String> groupBuyingType) {
        this.groupBuyingType = groupBuyingType;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public List<StoreResponse> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<StoreResponse> storeList) {
        this.storeList = storeList;
    }

    public List<Integer> getUserList() {
        return userList;
    }

    public void setUserList(List<Integer> userList) {
        this.userList = userList;
    }

    public Map<Integer, GroupUserStatusResponse> getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Map<Integer, GroupUserStatusResponse> userStatus) {
        this.userStatus = userStatus;
    }
}
