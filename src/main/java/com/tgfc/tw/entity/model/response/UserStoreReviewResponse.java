package com.tgfc.tw.entity.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tgfc.tw.entity.model.po.StoreReview;

public class UserStoreReviewResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer storeReviewId;
    private Integer storeId;
    private String storeName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer rank;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    public UserStoreReviewResponse() {}

    public UserStoreReviewResponse(StoreReview storeReview) {
        this.storeReviewId = storeReview.getId();
        this.storeId = storeReview.getStore().getId();
        this.storeName = storeReview.getStore().getName();
        this.rank = storeReview.getReview();
        this.message = storeReview.getMessage();
    }

    public Integer getStoreReviewId() {
        return storeReviewId;
    }

    public void setStoreReviewId(Integer storeReviewId) {
        this.storeReviewId = storeReviewId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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
