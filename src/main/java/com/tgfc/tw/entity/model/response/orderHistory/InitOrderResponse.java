package com.tgfc.tw.entity.model.response.orderHistory;

import com.tgfc.tw.entity.model.response.FloorResponse;
import com.tgfc.tw.entity.model.response.StoreNameResponse;

import java.util.List;

public class InitOrderResponse {

    private List<FloorResponse> floors;
    private List<StoreNameResponse> stores;

    public List<FloorResponse> getFloors() {
        return floors;
    }

    public void setFloors(List<FloorResponse> floors) {
        this.floors = floors;
    }

    public List<StoreNameResponse> getStores() {
        return stores;
    }

    public void setStores(List<StoreNameResponse> stores) {
        this.stores = stores;
    }
}
