package com.tgfc.tw.entity.model.response;

import java.util.List;

public class StoreOrderRecordResponse {
    private String storeName;
    private List<UserOrderDetailResponse> userOrderDetailResponseList;

    public StoreOrderRecordResponse(String storeName, List<UserOrderDetailResponse> userOrderDetailResponseList) {
        this.storeName = storeName;
        this.userOrderDetailResponseList = userOrderDetailResponseList;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<UserOrderDetailResponse> getUserOrderDetailResponseList() {
        return userOrderDetailResponseList;
    }

    public void setUserOrderDetailResponseList(List<UserOrderDetailResponse> userOrderDetailResponseList) {
        this.userOrderDetailResponseList = userOrderDetailResponseList;
    }
}
