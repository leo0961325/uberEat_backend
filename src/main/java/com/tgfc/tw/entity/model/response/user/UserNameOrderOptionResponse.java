package com.tgfc.tw.entity.model.response.user;

public class UserNameOrderOptionResponse {
    private int id;
    private String name;
    private String floor;
    private int optionId;
    private long count;

    public UserNameOrderOptionResponse(int id, String floor, String name, int optionId, long count) {
        this.id = id;
        this.name = name;
        this.floor = floor;
        this.optionId = optionId;
        this.count = count;
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

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
