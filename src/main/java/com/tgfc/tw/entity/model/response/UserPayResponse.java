package com.tgfc.tw.entity.model.response;

import com.tgfc.tw.entity.model.po.UserPay;

public class UserPayResponse {

    private int storeId;

    private boolean payStatus;

    private int pay;

    private int totalPrice;

    public UserPayResponse(){

    }

    public UserPayResponse(int storeId, boolean payStatus, int pay, int totalPrice){
        this.storeId = storeId;
        this.payStatus = payStatus;
        this.pay = pay;
        this.totalPrice = totalPrice;
    }

    public UserPayResponse(UserPay userPay){
        this.payStatus = userPay.isStatus();
        this.pay = userPay.getPay();
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public boolean isPayStatus() {
        return payStatus;
    }

    public void setPayStatus(boolean payStatus) {
        this.payStatus = payStatus;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
