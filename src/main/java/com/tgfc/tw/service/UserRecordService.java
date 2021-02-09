package com.tgfc.tw.service;

import com.tgfc.tw.entity.model.response.user.UserOrderRecordListResponse;
import com.tgfc.tw.entity.model.response.user.UserOrderRecordResponse;

import java.util.List;

public interface UserRecordService {

    UserOrderRecordListResponse getUserOrderWeekRecord();
}
