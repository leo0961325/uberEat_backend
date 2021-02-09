package com.tgfc.tw.entity.model.request;


import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

public class StoreReviewAddRequest {
    @Nullable
    private Integer storeReviewId;
    @NotNull
    private Integer storeId;
    @NotNull
    private Integer rank;
    @Nullable
    private String message;

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
