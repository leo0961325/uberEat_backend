package com.tgfc.tw.entity.cenum.group;

public enum FloorDefault {
    ALL(0, "全選");

    FloorDefault(int value, String text){
        this.value = value;
        this.text = text;
    }

    private int value;
    private String text;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
