package com.tgfc.tw.service;

import com.tgfc.tw.entity.model.request.AddStoreValueRequest;
import com.tgfc.tw.entity.model.response.pay.StoreValueRecordResponse;

import java.util.Map;

public interface PayService {

    void addStoreValue(AddStoreValueRequest request) throws Exception;

    Map getBalance() throws Exception;

    StoreValueRecordResponse getStoreValueRecordList(int memberId, int pageNumber, int pageSize, boolean sortMode) throws Exception;

    void checkPayments();
}
