package com.tgfc.tw.entity.model.response;

import java.util.List;

public class InitResponse {
    private List<TagResponse> tags;
    private List<TimeStatusResponse> timeTypeResponses;

    public InitResponse(List<TagResponse> tags, List<TimeStatusResponse> timeTypeResponses) {
        this.tags = tags;
        this.timeTypeResponses = timeTypeResponses;
    }

    public InitResponse(){}

    public List<TagResponse> getTags() {
        return tags;
    }

    public void setTags(List<TagResponse> tags) {
        this.tags = tags;
    }

    public List<TimeStatusResponse> getTimeTypeResponses() {
        return timeTypeResponses;
    }

    public void setTimeTypeResponses(List<TimeStatusResponse> timeTypeResponses) {
        this.timeTypeResponses = timeTypeResponses;
    }
}
