package com.tgfc.tw.entity.model.response;

import com.tgfc.tw.entity.model.po.Floor;

public class FloorResponse {
    private int id;
    private String name;

    public FloorResponse(int id, String floorName) {
        this.id = id;
        this.name = floorName;
    }

    public FloorResponse(Floor floor) {
        setId(floor.getId());
        setName(floor.getName());
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
}
