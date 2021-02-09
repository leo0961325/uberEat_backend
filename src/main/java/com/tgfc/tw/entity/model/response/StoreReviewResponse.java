package com.tgfc.tw.entity.model.response;

import java.util.List;

public class StoreReviewResponse {
    private Integer totalUsers;
    private Long totalMessage;
    private List<RankMessageResponse> rankList;

    public StoreReviewResponse() {}

    public StoreReviewResponse(Integer totalUsers, Long totalMessage, List<RankMessageResponse> rankList) {
        this.totalUsers = totalUsers;
        this.totalMessage = totalMessage;
        this.rankList = rankList;
    }

    public Integer getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Integer totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getTotalMessage() {
        return totalMessage;
    }

    public void setTotalMessage(Long totalMessage) {
        this.totalMessage = totalMessage;
    }

    public List<RankMessageResponse> getRankList() {
        return rankList;
    }

    public void setRankList(List<RankMessageResponse> rankList) {
        this.rankList = rankList;
    }
}
