package com.tgfc.tw.entity.model.response.orderHistory;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryCountByUserResponse {

    private String userName;
    private int id;
    private String name;
    private int price;
    private String option;
    private int optionPrice;
    private long count;
    private long totalPrice;
    private int userId;
    private int storeId;

    public OrderHistoryCountByUserResponse(String userName,int id, String name, int price, String option, int optionPrice, long count, long totalPrice, Integer userId, Integer storeId){
        this.userName = userName;
        this.id = id;
        this.name = name;
        this.price = price;
        this.option = option;
        this.optionPrice = optionPrice;
        this.count = count;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.storeId = storeId;
    }

    public OrderHistoryCountByUserResponse(){

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public int getOptionPrice() {
        return optionPrice;
    }

    public void setOptionPrice(int optionPrice) {
        this.optionPrice = optionPrice;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
