package com.tgfc.tw.entity.model.response.orderHistory;

import com.tgfc.tw.entity.model.response.StoreGetOneResponse;

import java.util.List;

public class OrderHistoryCountOrderResponse {

    private StoreGetOneResponse store;

    private List<OrderHistoryCountByOrderResponse> countByOrder;

    public StoreGetOneResponse getStore() {
        return store;
    }

    public void setStore(StoreGetOneResponse store) {
        this.store = store;
    }

    public List<OrderHistoryCountByOrderResponse> getCountByOrder() {
        return countByOrder;
    }

    public void setCountByOrder(List<OrderHistoryCountByOrderResponse> countByOrder) {
        this.countByOrder = countByOrder;
    }
}
