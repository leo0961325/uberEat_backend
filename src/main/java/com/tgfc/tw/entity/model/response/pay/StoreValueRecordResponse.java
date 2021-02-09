package com.tgfc.tw.entity.model.response.pay;



import org.springframework.data.domain.Page;

import java.util.List;

public class StoreValueRecordResponse {
    private MemberResponse member;
    //private List<PayRecordResponse> payRecord;
    private Page<PayRecordResponse> payRecord;

    public StoreValueRecordResponse(MemberResponse member, Page<PayRecordResponse> payRecord) {
        this.member = member;
        this.payRecord = payRecord;
    }

    public MemberResponse getMember() {
        return member;
    }

    public void setMember(MemberResponse member) {
        this.member = member;
    }

    public Page<PayRecordResponse> getPayRecord() {
        return payRecord;
    }

    public void setPayRecord(Page<PayRecordResponse> payRecord) {
        this.payRecord = payRecord;
    }
}
