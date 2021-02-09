package com.tgfc.tw.entity.model.response.orderHistory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgfc.tw.entity.model.po.Floor;
import com.tgfc.tw.entity.model.po.Option;
import com.tgfc.tw.entity.model.po.Product;
import com.tgfc.tw.entity.model.po.User;

public class OrderHistoryGetUserResponse {

    private int groupId;
    private int orderHistoryId;
    private int userId;
    private Option option;
    private Product product;
    private String name;
    @JsonProperty("floor")
    private String floorName;
    private long count;

    public OrderHistoryGetUserResponse(int groupId, int orderHistoryId, Option option, Product product, User user, Floor f, long count){
        this.groupId = groupId;
        this.orderHistoryId = orderHistoryId;
        this.option = option;
        this.product = product;
        if(user != null) {
            this.userId = user.getId();
            this.name = user.getEnglishName();
            this.floorName = f.getName();
        }
        this.count = count;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getOrderHistoryId() {
        return orderHistoryId;
    }

    public void setOrderHistoryId(int orderHistoryId) {
        this.orderHistoryId = orderHistoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
