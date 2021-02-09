package com.tgfc.tw.entity.model.request;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class OrderWithoutIdRequest {

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @Min(0)
    private int price;

    private String remark;

    private int storeId;

    private int groupId;

    public String getName() {
        return name.trim();
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "OrderWithoutIdRequest{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", remark='" + remark + '\'' +
                ", storeId=" + storeId +
                ", groupId=" + groupId +
                '}';
    }
}
