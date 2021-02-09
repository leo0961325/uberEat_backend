package com.tgfc.tw.entity.model.response;

import com.tgfc.tw.entity.model.po.Option;
import com.tgfc.tw.entity.model.po.Product;
import com.tgfc.tw.entity.model.response.order.OrderDetailResponse;


public class GroupUserOrderResponse {

    private int groupId;
    private int storeId;
    private OrderDetailResponse orderDetailResponse;

    public GroupUserOrderResponse(int groupId, int storeId, Product product, Option option, long count){
        this.groupId = groupId;
        this.storeId = storeId;
        this.orderDetailResponse = new OrderDetailResponse(product, option,(int) count);
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public OrderDetailResponse getOrderDetailResponse() {
        return orderDetailResponse;
    }

    public void setOrderDetailResponse(OrderDetailResponse orderDetailResponse) {
        this.orderDetailResponse = orderDetailResponse;
    }
}
