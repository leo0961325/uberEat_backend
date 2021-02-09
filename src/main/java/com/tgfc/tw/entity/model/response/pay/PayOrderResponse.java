package com.tgfc.tw.entity.model.response.pay;

import java.util.List;

public class PayOrderResponse {
    private String storeName;
    private List<PayOrderDetailResponse> orderDetail;

    public PayOrderResponse() {
    }

    public PayOrderResponse(String storeName, List<PayOrderDetailResponse> orderDetail) {
        this.storeName = storeName;
        this.orderDetail = orderDetail;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<PayOrderDetailResponse> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<PayOrderDetailResponse> orderDetail) {
        this.orderDetail = orderDetail;
    }
}
