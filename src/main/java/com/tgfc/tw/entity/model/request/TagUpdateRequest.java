package com.tgfc.tw.entity.model.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TagUpdateRequest {

    @Min(value = 1, message = "id不能為空")
    private int id;

    @NotNull
    @NotEmpty(message = "名稱不能空白")
    private String name;


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

}
