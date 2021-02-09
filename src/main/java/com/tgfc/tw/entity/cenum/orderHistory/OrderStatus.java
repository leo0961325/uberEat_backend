package com.tgfc.tw.entity.cenum.orderHistory;

public enum OrderStatus {
    PASS(-1),
    NOT_ORDER(0),
    ORDERED(1)
    ;


    private int value;

    OrderStatus(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
