package com.tgfc.tw.entity.model.request;


import com.tgfc.tw.entity.model.po.Product;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OrderWithoutStoreIdRequest {
    private int id;

    @NotEmpty(message = "Name cannot be empty")
    @NotNull
    private String name;

    private String remark;

    @Min(0)
    private int price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name.trim();
    }

    public void setName(String name) {
        this.name = name.trim();
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

    public boolean isSame(Product product) {
        return getName().equals(product.getName())
                && getPrice() == product.getPrice()
                && getRemark() == product.getRemark();
    }
}
