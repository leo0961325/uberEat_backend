package com.tgfc.tw.service;

import com.tgfc.tw.entity.model.request.UserDailyRecordRequest;
import com.tgfc.tw.entity.model.response.user.UserAllOrderRecordResponse;
import com.tgfc.tw.entity.model.response.user.UserOrderRecordGroupResponse;
import org.springframework.data.domain.Page;



public interface UserAllRecordService {

    Page<UserOrderRecordGroupResponse> getUserAllRecord(UserDailyRecordRequest model);
}
