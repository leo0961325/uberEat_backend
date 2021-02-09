package com.tgfc.tw.entity.model.response.order;


import com.tgfc.tw.entity.model.po.Product;

public class OrderResponse {
    private int id;
    private String name;
    private int price;
    private String remark;

    public OrderResponse(){

    }

    public OrderResponse(Product product){
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.remark = product.getRemark();
    }

    public OrderResponse(int id, String name, int price, String remark){
        this.id=id;
        this.name=name;
        this.price=price;
        this.remark = remark;
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
}
