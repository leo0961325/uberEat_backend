package com.tgfc.tw.entity.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tgfc.tw.entity.cenum.orderHistory.OrderStatus;
import com.tgfc.tw.entity.model.response.orderHistory.OrderHistoryCountByUserResponse;
import com.tgfc.tw.entity.model.response.orderHistory.OrderStatusResponse;

import java.util.List;
import java.util.Optional;

public class OrderHistoryUserCountResponseV2_1 {

    private int userId;
    private String userName;
    private long totalPrice;
    private int totalPay;
    private List<OrderHistoryCountByUserResponse> countByUser;
    @JsonIgnore
    private List<OrderStatusResponse> orderStatus;


    public OrderHistoryUserCountResponseV2_1(int userId, String userName, long totalPrice, int totalPay, List<OrderHistoryCountByUserResponse> countByUser) {
        this.userId = userId;
        this.userName = userName;
        this.totalPrice = totalPrice;
        this.totalPay = totalPay;
        this.countByUser = countByUser;
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
