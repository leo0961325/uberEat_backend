package com.tgfc.tw.entity.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tgfc.tw.entity.model.po.Store;
import com.tgfc.tw.entity.model.po.User;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.List;

public class GroupRequest {
    private int id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp endTime;
    private List<Integer> userList;
    private List<Integer> floorIdList;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Timestamp openDate;


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

    public Timestamp getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Timestamp openDate) {
        this.openDate = openDate;
    }
}
