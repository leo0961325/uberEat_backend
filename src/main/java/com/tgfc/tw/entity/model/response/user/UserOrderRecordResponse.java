package com.tgfc.tw.entity.model.response.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgfc.tw.entity.model.response.RecordResponse;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

public class UserOrderRecordResponse {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp openDate;
    private int day;
    @JsonProperty("detail")
    private List<RecordResponse> recordResponseList;
    private int sum;

    public UserOrderRecordResponse() {
    }

    public UserOrderRecordResponse(Timestamp openDate, int day,List<RecordResponse> recordResponseList,int sum) {
        this.openDate = openDate;
        this.day = day;
        this.recordResponseList = recordResponseList;
        this.sum = sum;
    }

    public Timestamp getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Timestamp openDate) {
        this.openDate = openDate;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<RecordResponse> getRecordResponseList() {
        return recordResponseList;
    }

    public void setRecordResponseList(List<RecordResponse> recordResponseList) {
        this.recordResponseList = recordResponseList;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
