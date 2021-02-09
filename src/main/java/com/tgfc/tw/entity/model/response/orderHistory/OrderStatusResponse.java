package com.tgfc.tw.entity.model.response.orderHistory;

public class OrderStatusResponse {
    private int userId;
    private int storeId;
    private Integer userStatus;

    public OrderStatusResponse() {

    }

    public OrderStatusResponse(int userId, int storeId) {
        this.userId = userId;
        this.storeId = storeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }
}
