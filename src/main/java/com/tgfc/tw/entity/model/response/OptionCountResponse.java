package com.tgfc.tw.entity.model.response;

import com.tgfc.tw.entity.model.po.Option;
import com.tgfc.tw.entity.model.po.Order;
import com.tgfc.tw.entity.model.response.user.UserNameResponse;

import java.util.ArrayList;
import java.util.List;

public class OptionCountResponse {

    private int id;
    private String name;
    private int price;
    private int count;
    private int totalPrice;
    private int orderPrice;
    private UserNameResponse user;

    public OptionCountResponse() {}

    public OptionCountResponse(int id, String name, int price,int productPrice, int count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.orderPrice = productPrice;
        this.totalPrice = orderPrice + this.price;
    }

    public OptionCountResponse(int id, String name, int price, int count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.totalPrice = price * count;
    }

    public OptionCountResponse(Option option, int count) {
        this.id = option.getId();
        this.name = option.getName();
        this.price = option.getPrice();
        this.count = count;
        this.orderPrice = option.getProduct().getPrice();
        this.totalPrice = orderPrice + price;
    }

    public static List<OptionCountResponse> valueOf(List<Option> option, Order oh) {
        List<OptionCountResponse> responses = new ArrayList<>();
        for (Option item : option) {
            OptionCountResponse optionResponse = new OptionCountResponse();
            optionResponse.setId(item.getId());
            optionResponse.setName(item.getName());
            optionResponse.setPrice(item.getPrice());
            int optionCount = oh.getUserList().size();
            optionResponse.setCount(optionCount);
            optionResponse.setTotalPrice(item.getPrice() * optionCount);
            responses.add(optionResponse);
        }
        return responses;
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

    public UserNameResponse getUser() {
        return user;
    }

    public void setUser(UserNameResponse user) {
        this.user = user;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }
}
