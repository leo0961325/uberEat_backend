package com.tgfc.tw.entity.model.response.order;


import com.tgfc.tw.entity.model.po.Option;
import com.tgfc.tw.entity.model.po.Product;

import java.math.BigInteger;
import java.util.Map;

public class OrderDetailResponse {
    private int id;
    private String name;
    private String optionName;
    private int count;
    private int price;
    private int totalPrice;
    private String remark;

    public static OrderDetailResponse valueOf(Map<String, Object> mUserStatus) {
        OrderDetailResponse res = new OrderDetailResponse();
        res.setId((int) mUserStatus.get("productId"));
        res.setName((String) mUserStatus.get("productName"));
        res.setOptionName((String) mUserStatus.get("optionName"));
        res.setCount(((BigInteger) mUserStatus.get("itemCount")).intValue());
        res.setPrice(((int) mUserStatus.get("productPrice")) + ((int) mUserStatus.get("optionPrice")));
        res.setTotalPrice(res.getCount() * res.getPrice());
        return res;
    }

    public OrderDetailResponse() {
    }

    public OrderDetailResponse(int productId, String productName, String optionName, BigInteger itemCount, int productPrice, int optionPrice, String productRemark) {
        this.id = productId;
        this.name = productName;
        this.optionName = optionName;
        this.price = productPrice + optionPrice;
        this.remark = productRemark;
        this.count = itemCount.intValue();
        this.totalPrice = count * price;
    }

    public OrderDetailResponse(Product product, Option option, int count) {
        this.id = product.getId();
        this.name = product.getName();
        this.optionName = option.getName();
        this.price = product.getPrice() + option.getPrice();
        this.remark = product.getRemark();
        this.count = count;
        this.totalPrice = count * price;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }
}
