package com.tgfc.tw.entity.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tgfc.tw.entity.model.po.Group;
import com.tgfc.tw.entity.model.po.Store;
import com.tgfc.tw.entity.model.po.Tag;
import com.tgfc.tw.entity.model.response.user.UserNameOnlyResponse;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupResponse {

    private int id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp endTime;
    private List<Tag> tagList;
    private boolean isLocked;
    private int timeStatus;
    private List<FloorResponse> floorResponses;
    private List<StoreResponse> storeList;
    private List<StorePictureResponse> storePicList;
    private List<UserNameOnlyResponse> userList;
    private Map<Integer, GroupUserStatusResponse> userStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp openDate;

    public GroupResponse() {
    }

    public GroupResponse(int id, String name, Timestamp startTime, Timestamp endTime, boolean isLocked, Timestamp openDate) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isLocked = isLocked;
        this.timeStatus = getTimeForGroupList(startTime, endTime, isLocked, openDate);
        this.openDate = openDate;
    }

    public GroupResponse(Group group) {
        id = group.getId();
        name = group.getName();
        startTime = group.getStartTime();
        endTime = group.getEndTime();
        isLocked = group.isLocked();
        openDate = group.getOpenDate();

        floorResponses = group.getFloorList()
                .stream()
                .map(FloorResponse::new)
                .collect(Collectors.toList());

        storePicList = group.getStoreList()
                .stream()
                .map(Store::getStorePictureList)
                .flatMap(List::stream).collect(Collectors.toList())
                .stream().map(StorePictureResponse::new)
                .collect(Collectors.toList());

        if (!(group.getUserList() == null || group.getUserList().size() == 0)) {
            userList = group.getUserList()
                    .stream()
                    .map(UserNameOnlyResponse::new)
                    .collect(Collectors.toList());
        }

        storeList = group.getStoreList()
                .stream()
                .map(StoreResponse::new)
                .collect(Collectors.toList());

        timeStatus = getTime(group);
    }

    private int getTimeForGroupList(Timestamp startTime, Timestamp endTime, boolean isLocked, Timestamp openDate) {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        if (timestamp.before(startTime)) {
            return 3;
        } else if (timestamp.after(startTime) && (endTime == null || timestamp.before(endTime)) && isLocked ||
                (timestamp.after(endTime) && timestamp.toLocalDateTime().toLocalDate().equals(endTime.toLocalDateTime().toLocalDate())) ||
                (timestamp.after(endTime) && timestamp.toLocalDateTime().toLocalDate().equals(openDate.toLocalDateTime().toLocalDate()))) {
            return 0;
        } else if (timestamp.after(startTime) && (endTime == null || timestamp.before(endTime)) && !isLocked) {
            if ((endTime != null && timestamp.toLocalDateTime().toLocalDate().equals(endTime.toLocalDateTime().toLocalDate())))
                return 1;
            else
                return 2;
        } else if (timestamp.after(endTime)) {
            return 4;
        }
        return 4;
    }

    private int getTime(Group group) {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        if (timestamp.before(group.getStartTime())) {
            return 3;
        } else if (timestamp.after(group.getStartTime()) && (endTime == null ||
                timestamp.before(group.getEndTime())) && group.getIsLocked() ||
                (timestamp.after(group.getEndTime()) && timestamp.toLocalDateTime().toLocalDate().equals(group.getEndTime().toLocalDateTime().toLocalDate()))) {
            return 0;
        } else if (timestamp.after(group.getStartTime()) && (endTime == null || timestamp.before(group.getEndTime())) && !group.getIsLocked()) {
            if ((endTime != null && timestamp.toLocalDateTime().toLocalDate().equals(group.getEndTime().toLocalDateTime().toLocalDate())))
                return 1;
            else
                return 2;
        } else if (timestamp.after(group.getEndTime())) {
            return 4;
        }
        return 4;
    }

    public static GroupResponse valueOf(Group group) {
        return new GroupResponse(group);
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

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public List<StorePictureResponse> getStorePicList() {
        return storePicList;
    }

    public void setStorePicList(List<StorePictureResponse> storePicList) {
        this.storePicList = storePicList;
    }

    public List<UserNameOnlyResponse> getUserList() {
        return userList;
    }

    public void setUserList(List<UserNameOnlyResponse> userList) {
        this.userList = userList;
    }

    public List<StoreResponse> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<StoreResponse> storeList) {
        this.storeList = storeList;
    }

    public Map<Integer, GroupUserStatusResponse> getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Map<Integer, GroupUserStatusResponse> userStatus) {
        this.userStatus = userStatus;
    }

    public int getTimeStatus() {
        return timeStatus;
    }

    public void setTimeStatus(int timeStatus) {
        this.timeStatus = timeStatus;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public List<FloorResponse> getFloorResponses() {
        return floorResponses;
    }

    public void setFloorResponses(List<FloorResponse> floorResponses) {
        this.floorResponses = floorResponses;
    }

    public Timestamp getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Timestamp openDate) {
        this.openDate = openDate;
    }
}
