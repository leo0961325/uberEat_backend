package com.tgfc.tw.entity.cenum.pay;

public enum PayTypeEnum {
    PAY_TYPE_CASH(1,"現金"),
    PAY_TYPE_TRANSFER(2,"轉帳"),
    PAY_TYPE_LINE_PAY(3,"LinePay"),
    PAY_TYPE_OTHER(4,"其他");




    private int type;
    private String trans;

    PayTypeEnum(int type, String trans) {
        this.type = type;
        this.trans = trans;
    }

    PayTypeEnum() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }
}
