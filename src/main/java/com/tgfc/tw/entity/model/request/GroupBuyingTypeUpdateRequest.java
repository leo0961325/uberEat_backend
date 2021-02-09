package com.tgfc.tw.entity.model.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class GroupBuyingTypeUpdateRequest {

    @Min(value = 1, message = "id不能為空")
    private int id;

    @NotBlank(message = "名稱不能空白")
    private String name;


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
}
