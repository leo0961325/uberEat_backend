package com.tgfc.tw.entity.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;

public class OptionAddRequest {

    private String name;

    private int price;

    @JsonProperty("orderId")
    @Min(value = 1, message = "productId 不能為空")
    private int productId;

    @Min(value = 1, message = "groupId 不能為空")
    private int groupId;

    private int userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
