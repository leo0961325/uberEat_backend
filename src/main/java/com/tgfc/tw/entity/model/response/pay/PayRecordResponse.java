package com.tgfc.tw.entity.model.response.pay;

import com.tgfc.tw.entity.model.po.UserPay;

import java.time.LocalDateTime;
import java.util.List;

public class PayRecordResponse {
    private int id;
    private LocalDateTime dateTime;
    private boolean status;
    private int payType;
    private int pay;
    private String remark;
    private String manager;
    private int totalPrice;
    private List<PayOrderResponse> order;



    public static PayRecordResponse valueOf(UserPay userPay){
        PayRecordResponse payRecordResponse = new PayRecordResponse();
        payRecordResponse.setId(userPay.getId());
        payRecordResponse.setDateTime(userPay.getDate());
        payRecordResponse.setStatus(userPay.isStatus());
        payRecordResponse.setPayType(userPay.getPayType());
        payRecordResponse.setPay(userPay.getPay());
        payRecordResponse.setRemark(userPay.getRemark());
        payRecordResponse.setManager(userPay.getManager());
        return payRecordResponse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<PayOrderResponse> getOrder() {
        return order;
    }

    public void setOrder(List<PayOrderResponse> order) {
        this.order = order;
    }
}
