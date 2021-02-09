package com.tgfc.tw.entity.model.response.orderHistory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgfc.tw.entity.model.po.Group;
import com.tgfc.tw.entity.model.response.GroupUserStatusResponse;
import com.tgfc.tw.entity.model.response.TagResponse;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class OrderHistoryGroupResponse {
    private int id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp endTime;
    private boolean isLocked;
    private List<TagResponse> tagList;
    @JsonProperty("store")
    private List<OrderHistoryStoreResponse> store;
    private Map<Integer, GroupUserStatusResponse> userStatus;
    private boolean isManager;

    public OrderHistoryGroupResponse() {
    }

    public OrderHistoryGroupResponse(int id, String name, Timestamp startTime, Timestamp endTime, boolean isLocked) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isLocked = isLocked;
    }

    public static OrderHistoryGroupResponse valueOf(Group group, int userFloorId, boolean isManager) {
        OrderHistoryGroupResponse item = new OrderHistoryGroupResponse();
        BeanUtils.copyProperties(group, item);
        List<OrderHistoryUserRespense> histories = OrderHistoryUserRespense.valueListOf(group.getOrderList(), userFloorId, isManager);
        item.setStore(OrderHistoryStoreResponse.valueListOf(group.getStoreList(), histories));
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
    public List<OrderHistoryStoreResponse> getStore() {
        return store;
    }
    public void setStore(List<OrderHistoryStoreResponse> store) {
        this.store = store;
    }
    public boolean isLocked() { return isLocked; }
    public void setLocked(boolean locked) { isLocked = locked; }
    public Map<Integer, GroupUserStatusResponse> getUserStatus() { return userStatus; }
    public void setUserStatus(Map<Integer, GroupUserStatusResponse> userStatus) { this.userStatus = userStatus; }
    public List<TagResponse> getTagList() {
        return tagList;
    }
    public void setTagList(List<TagResponse> tagList) {
        this.tagList = tagList;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
}
