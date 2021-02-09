package com.tgfc.tw.entity.model.response.orderHistory;

import com.tgfc.tw.entity.model.response.StoreGetOneResponse;
import com.tgfc.tw.entity.model.response.StoreGetOrderDetailResponse;

import java.util.List;

public class OrderHistoryCountUserResponse {

    private List<StoreGetOrderDetailResponse> stores;

    private List<OrderHistoryUserCountResponse> userCountByGroup;

    public List<StoreGetOrderDetailResponse> getStores() {
        return stores;
    }

    public void setStores(List<StoreGetOrderDetailResponse> stores) {
        this.stores = stores;
    }

    public List<OrderHistoryUserCountResponse> getUserCountByGroup() {
        return userCountByGroup;
    }

    public void setUserCountByGroup(List<OrderHistoryUserCountResponse> userCountByGroup) {
        this.userCountByGroup = userCountByGroup;
    }
}
