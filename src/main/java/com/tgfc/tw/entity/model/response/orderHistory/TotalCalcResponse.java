package com.tgfc.tw.entity.model.response.orderHistory;

public class TotalCalcResponse {
    private long tOrderCount;
    private long tOrderPrice;

    public TotalCalcResponse(){

    }

    public TotalCalcResponse(long orderCount, Long orderPrice){
        this.tOrderCount = orderCount;
        this.tOrderPrice = orderPrice != null ? orderPrice : 0;
    }

    public long gettOrderCount() {
        return tOrderCount;
    }

    public void settOrderCount(long tOrderCount) {
        this.tOrderCount = tOrderCount;
    }

    public long gettOrderPrice() {
        return tOrderPrice;
    }

    public void settOrderPrice(long tOrderPrice) {
        this.tOrderPrice = tOrderPrice;
    }
}
