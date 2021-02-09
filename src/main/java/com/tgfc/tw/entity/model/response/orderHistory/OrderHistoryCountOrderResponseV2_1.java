package com.tgfc.tw.entity.model.response.orderHistory;

import com.tgfc.tw.entity.model.response.StoreGetOneResponse;

import java.util.List;

public class OrderHistoryCountOrderResponseV2_1 {
    private StoreGetOneResponse store;

    private List<OrderHistoryCountByOrderResponseV2_1> countByOrder;

    public StoreGetOneResponse getStore() {
        return store;
    }

    public void setStore(StoreGetOneResponse store) {
        this.store = store;
    }

    public List<OrderHistoryCountByOrderResponseV2_1> getCountByOrder() {
        return countByOrder;
    }

    public void setCountByOrder(List<OrderHistoryCountByOrderResponseV2_1> countByOrder) {
        this.countByOrder = countByOrder;
    }
}
