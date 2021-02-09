package com.tgfc.tw.entity.model.request;

import javax.validation.constraints.*;

public class AddStoreValueRequest {
    @Min(value = 1,message = "金額不得小於0")
    @NotNull(message = "金額不得為空")
    private int pay;
    private boolean status;
    private int payType;
    @NotNull(message = "人員id不得為空")
    private Integer memberId;
    private String remark;


    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
