package com.tgfc.tw.entity.model.response.user;

import com.tgfc.tw.entity.model.response.UserOrderDetailResponse;

import java.util.List;

public class UserAllOrderRecordResponse {
    private List<UserOrderRecordGroupResponse> userOrderRecordGroupResponseList;


    public UserAllOrderRecordResponse(List<UserOrderRecordGroupResponse> userOrderRecordGroupResponseList) {
        this.userOrderRecordGroupResponseList = userOrderRecordGroupResponseList;
    }

    public List<UserOrderRecordGroupResponse> getUserOrderRecordGroupResponseList() {
        return userOrderRecordGroupResponseList;
    }

    public void setUserOrderRecordGroupResponseList(List<UserOrderRecordGroupResponse> userOrderRecordGroupResponseList) {
        this.userOrderRecordGroupResponseList = userOrderRecordGroupResponseList;
    }
}
