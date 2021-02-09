package com.tgfc.tw.entity.model.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TagAddRequest {
    private int id;

    @NotNull
    @NotEmpty(message = "名稱不能空白")
    private String name;

    public String getName() {
        return name.trim();
    }

    public void setName(String name) {
        this.name = name.trim();
    }
}
