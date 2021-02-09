package com.tgfc.tw.entity.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.util.List;

public class GroupWithoutIdRequest {

    private String name;
    private List<Integer> storeList;
    private List<Integer> userList;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp endTime;
    private List<Integer> floorIdList;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp openDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Integer> storeList) {
        this.storeList = storeList;
    }

    public List<Integer> getUserList() {
        return userList;
    }

    public void setUserList(List<Integer> userList) {
        this.userList = userList;
    }

    public List<Integer> getFloorIdList() {
        return floorIdList;
    }

    public void setFloorIdList(List<Integer> floorIdList) {
        this.floorIdList = floorIdList;
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

    public Timestamp getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Timestamp openDate) {
        this.openDate = openDate;
    }
}
