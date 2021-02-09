package com.tgfc.tw.entity.model.response.orderHistory;

import com.tgfc.tw.entity.model.response.OrderHistoryUserCountResponseV2_1;
import com.tgfc.tw.entity.model.response.StoreGetOrderDetailResponse;
import com.tgfc.tw.entity.model.response.StoreGetOrderDetailResponseV2_1;

import java.util.List;

public class OrderHistoryCountUserResponseV2_1 {

    private List<StoreGetOrderDetailResponseV2_1> stores;

    private List<OrderHistoryUserCountResponseV2_1> userCountByGroup;

    public List<StoreGetOrderDetailResponseV2_1> getStores() {
        return stores;
    }

    public void setStores(List<StoreGetOrderDetailResponseV2_1> stores) {
        this.stores = stores;
    }

    public List<OrderHistoryUserCountResponseV2_1> getUserCountByGroup() {
        return userCountByGroup;
    }

    public void setUserCountByGroup(List<OrderHistoryUserCountResponseV2_1> userCountByGroup) {
        this.userCountByGroup = userCountByGroup;
    }
}
