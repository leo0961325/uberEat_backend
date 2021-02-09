package com.tgfc.tw.entity.model.response;



import java.math.BigInteger;


public class RecordResponse {

    private String productName;
    private int count;
    private int totalPrice;


    public RecordResponse(String productName,  BigInteger itemCount,  int productPrice, int optionPrice) {
        this.productName = productName;
        this.count =itemCount.intValue();
        this.totalPrice = count * (productPrice + optionPrice);

    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }


}
