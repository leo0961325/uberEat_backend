package com.tgfc.tw.entity.model.response.orderHistory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tgfc.tw.entity.model.po.Order;
import com.tgfc.tw.entity.model.po.Product;
import com.tgfc.tw.entity.model.response.user.UserNameOrderOptionResponse;

import java.util.List;

public class OrderHistoryGetCountByOrderResponse {
    private int id;
    private String name;
    private String remark;
    private int price;
    private long count;
    private long optionAllPrice;
    private long orderTotalPrice;
    private List<UserNameOrderOptionResponse> users;
    @JsonIgnore
    private int orderHistoryId;

    public OrderHistoryGetCountByOrderResponse(){

    }

    public OrderHistoryGetCountByOrderResponse(Product product, Order order) {
        this.id = product.getId();
        this.name = product.getName();
        this.remark = product.getRemark();
        this.price = product.getPrice();
        this.orderHistoryId= order != null ? order.getId() : 0;
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

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getOptionAllPrice() {
        return optionAllPrice;
    }

    public void setOptionAllPrice(long optionAllPrice) {
        this.optionAllPrice = optionAllPrice;
    }

    public long getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(long orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public List<UserNameOrderOptionResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserNameOrderOptionResponse> users) {
        this.users = users;
    }

    public int getOrderHistoryId() {
        return orderHistoryId;
    }

    public void setOrderHistoryId(int orderHistoryId) {
        this.orderHistoryId = orderHistoryId;
    }
}
