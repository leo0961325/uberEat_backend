package com.tgfc.tw.entity.model.response;

import java.util.List;

public class GroupTimeResponse {

    private int timeStatus;
    private List<GroupResponse> groupResponses;

    public GroupTimeResponse(int timeStatus, GroupResponse group) {
        this.timeStatus = timeStatus;
        this.groupResponses.add(group);
    }

    public GroupTimeResponse() {
    }

    public int getTimeStatus() {
        return timeStatus;
    }

    public void setTimeStatus(int timeStatus) {
        this.timeStatus = timeStatus;
    }

    public List<GroupResponse> getGroupResponses() {
        return groupResponses;
    }

    public void setGroupResponses(List<GroupResponse> groupResponses) {
        this.groupResponses = groupResponses;
    }
}
