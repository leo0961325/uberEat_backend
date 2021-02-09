package com.tgfc.tw.entity.model.response;

import com.tgfc.tw.entity.model.po.Role;
import com.tgfc.tw.entity.model.po.Tag;

public class RoleResponse {
    private int id;
    private String code;
    private String name;

    public RoleResponse() {

    }

    public RoleResponse(Role role) {
        this.id = role.getId();
        this.code = role.getCode();
        this.name = role.getName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
