package com.tgfc.tw.entity.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgfc.tw.entity.model.response.order.OrderDetailResponse;

import java.util.List;

public class GroupUserStatusResponse {
    private int status;
    @JsonProperty("detail")
    private List<OrderDetailResponse> orderDetailResponsesList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<OrderDetailResponse> getOrderDetailResponsesList() {
        return orderDetailResponsesList;
    }

    public void setOrderDetailResponsesList(List<OrderDetailResponse> orderDetailResponsesList) {
        this.orderDetailResponsesList = orderDetailResponsesList;
    }
}
