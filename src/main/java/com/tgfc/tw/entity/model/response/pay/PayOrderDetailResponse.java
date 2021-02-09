package com.tgfc.tw.entity.model.response.pay;

public class PayOrderDetailResponse {
    private String productName;
    private int count;
    private int price;
    private String optionName;

    public PayOrderDetailResponse() {
    }

    public PayOrderDetailResponse(String productName, int count, int productPrice, int optionPrice, String optionName) {
        this.productName = productName;
        this.count = count;
        this.price = productPrice+optionPrice;
        this.optionName = optionName;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }
}
