package com.tgfc.tw.entity.model.response.user;

import java.util.List;

public class UserOrderRecordListResponse {

    private List<UserOrderRecordResponse> userOrderRecordResponseList;

    public UserOrderRecordListResponse(List<UserOrderRecordResponse> userOrderRecordResponseList) {
        this.userOrderRecordResponseList = userOrderRecordResponseList;
    }

    public List<UserOrderRecordResponse> getUserOrderRecordResponseList() {
        return userOrderRecordResponseList;
    }

    public void setUserOrderRecordResponseList(List<UserOrderRecordResponse> userOrderRecordResponseList) {
        this.userOrderRecordResponseList = userOrderRecordResponseList;
    }
}
