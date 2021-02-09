package com.tgfc.tw.entity.model.response.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tgfc.tw.entity.model.response.UserOrderDetailResponse;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class UserOrderRecordGroupResponse {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp openDate;
    private boolean isLocked;
    private String groupName;
    private int groupId;
    private int timeStatus;
    List<UserOrderDetailResponse> groupOrderDetailList;
    private int groupSum;



    public UserOrderRecordGroupResponse(Timestamp openDate, Timestamp startTime, Timestamp endTime, boolean isLocked, String groupName, int groupId, List<UserOrderDetailResponse>  groupOrderDetailList, int groupSum) {
        this.openDate = openDate;
        this.isLocked = isLocked;
        this.groupName = groupName;
        this.groupId = groupId;
        this.timeStatus = getTimeForGroupList(startTime,endTime,isLocked);
        this. groupOrderDetailList =  groupOrderDetailList;
        this.groupSum = groupSum;
    }
    private int getTimeForGroupList(Timestamp startTime,Timestamp endTime,boolean isLocked) {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        if (timestamp.before(startTime)) {
            return 3;
        } else if (timestamp.after(startTime) && (
                endTime == null || timestamp.before(endTime)) &&
                isLocked || (timestamp.after(endTime) &&
                timestamp.toLocalDateTime().toLocalDate().equals(endTime.toLocalDateTime().toLocalDate()))) {
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

    public Timestamp getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Timestamp openDate) {
        this.openDate = openDate;
    }



    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getTimeStatus() {
        return timeStatus;
    }

    public void setTimeStatus(int timeStatus) {
        this.timeStatus = timeStatus;
    }

    public List<UserOrderDetailResponse> getGroupOrderDetailList() {
        return groupOrderDetailList;
    }

    public void setGroupOrderDetailList(List<UserOrderDetailResponse> groupOrderDetailList) {
        this.groupOrderDetailList = groupOrderDetailList;
    }

    public int getGroupSum() {
        return groupSum;
    }

    public void setGroupSum(int groupSum) {
        this.groupSum = groupSum;
    }
}
