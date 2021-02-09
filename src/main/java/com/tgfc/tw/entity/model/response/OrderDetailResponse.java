package com.tgfc.tw.entity.model.response;

import java.math.BigInteger;

public class OrderDetailResponse {

    private String optionName;
    private int count;
    private int price;
    private int totalPrice;
    private String productName;



        public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public OrderDetailResponse(String optionName, BigInteger itemCount, int productPrice, int optionPrice, String productName) {
        this.optionName = optionName;
        this.count = itemCount.intValue();
        this.price = productPrice + optionPrice;
        this.totalPrice = count*price;

        this.productName = productName;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }


}

