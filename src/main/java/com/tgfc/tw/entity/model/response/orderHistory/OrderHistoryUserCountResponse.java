package com.tgfc.tw.entity.model.response.orderHistory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tgfc.tw.entity.cenum.orderHistory.OrderStatus;
import com.tgfc.tw.entity.model.response.UserPayResponse;

import java.util.List;
import java.util.Optional;

public class OrderHistoryUserCountResponse {

    private int userId;
    private String userName;
    private long totalPrice;
    private int totalPay;
    private List<OrderHistoryCountByUserResponse> countByUser;
    private List<UserPayResponse> userPay;
    @JsonIgnore
    private List<OrderStatusResponse> orderStatus;

    public OrderHistoryUserCountResponse(){

    }

    public OrderHistoryUserCountResponse(int userId, String userName, long totalPrice, int totalPay, List<OrderHistoryCountByUserResponse> countByUser, List<UserPayResponse> userPay){
        this.userId = userId;
        this.userName = userName;
        this.totalPrice = totalPrice;
        this.totalPay = totalPay;
        this.countByUser = countByUser;
        this.userPay = userPay;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(int totalPay) {
        this.totalPay = totalPay;
    }

    public List<OrderHistoryCountByUserResponse> getCountByUser() {
        return countByUser;
    }

    public void setCountByUser(List<OrderHistoryCountByUserResponse> countByUser) {
        this.countByUser = countByUser;
    }

    public List<UserPayResponse> getUserPay() {
        return userPay;
    }

    public void setUserPay(List<UserPayResponse> userPay) {
        this.userPay = userPay;
    }

    public List<OrderStatusResponse> getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(List<OrderStatusResponse> orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getSortCount(int storeId){
        Optional<OrderStatusResponse> first = this.orderStatus.stream().filter(r -> r.getStoreId() == storeId).findFirst();

        if(first.isPresent())
            return first.get().getUserStatus();
        return OrderStatus.NOT_ORDER.getValue();
    }
}
