package com.tgfc.tw.entity.model.request;


import java.security.Timestamp;

public class OrderHistoryModel {
    private int id;
    private Timestamp date;





    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
