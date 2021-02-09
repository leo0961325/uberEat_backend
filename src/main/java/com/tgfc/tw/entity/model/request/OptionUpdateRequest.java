package com.tgfc.tw.entity.model.request;

import javax.validation.constraints.Min;

public class OptionUpdateRequest {

    @Min(value = 1, message = "id 不能為空")
    private int id;

    private String name;

    private int price;

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

}
