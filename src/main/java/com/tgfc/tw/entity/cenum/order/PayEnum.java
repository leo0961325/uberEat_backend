package com.tgfc.tw.entity.cenum.order;

public enum PayEnum {
    PAY_BY_CASH(1,"現金付款"),
    PAY_BY_TRANSFER(2,"轉帳付款"),
    CANCEL_PAY_THIS_ORDER(3,"已取消付款");

    private int value;
    private String message;

    PayEnum(int value,String message){
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
