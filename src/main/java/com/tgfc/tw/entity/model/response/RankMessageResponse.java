package com.tgfc.tw.entity.model.response;

import com.tgfc.tw.entity.model.po.StoreReview;

import java.time.LocalDateTime;

public class RankMessageResponse {

    private String userEnglishName;
    private LocalDateTime date;
    private Integer rank;
    private String message;

    public RankMessageResponse() {}

    public RankMessageResponse(StoreReview storeReview) {
        this.userEnglishName = storeReview.getUser().getEnglishName();
        this.date = storeReview.getDate().toLocalDateTime();
        this.rank = storeReview.getReview();
        this.message = storeReview.getMessage();
    }

    public String getUserEnglishName() {
        return userEnglishName;
    }

    public void setUserEnglishName(String userEnglishName) {
        this.userEnglishName = userEnglishName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
