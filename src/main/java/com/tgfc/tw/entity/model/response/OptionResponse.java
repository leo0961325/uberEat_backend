package com.tgfc.tw.entity.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgfc.tw.entity.model.po.Option;

public class OptionResponse {

    private int id;

    private String name;

    private int price;

    @JsonProperty("orderId")
    private int productId;

    public OptionResponse(){

    }

    public OptionResponse(Option option) {

        this.id = option.getId();
        this.name = option.getName();
        this.price = option.getPrice();
        this.productId = option.getProduct().getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
}
