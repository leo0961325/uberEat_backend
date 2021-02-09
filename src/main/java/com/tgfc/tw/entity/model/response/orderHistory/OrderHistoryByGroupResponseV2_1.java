package com.tgfc.tw.entity.model.response.orderHistory;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderHistoryByGroupResponseV2_1 {

    @JsonProperty("orderId")
    private int productId;
    @JsonProperty("orderName")
    private String productName;
    @JsonProperty("orderPrice")
    private int productPrice;
    private int optionId;
    private String optionName;
    private int optionPrice;
    @JsonProperty("orderHistoryId")
    private int orderId;
    private int count;

    public OrderHistoryByGroupResponseV2_1(){

    }

    public OrderHistoryByGroupResponseV2_1(int productId, String productName, int productPrice, int optionId, String optionName, int optionPrice, int orderId, int count) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.optionId = optionId;
        this.optionName = optionName;
        this.optionPrice = optionPrice;
        this.orderId = orderId;
        this.count = count;
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getOptionPrice() {
        return optionPrice;
    }

    public void setOptionPrice(int optionPrice) {
        this.optionPrice = optionPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
