package com.tgfc.tw.service;

import com.tgfc.tw.entity.model.request.StoreReviewAddRequest;
import com.tgfc.tw.entity.model.response.StoreReviewResponse;
import com.tgfc.tw.entity.model.response.UserStoreReviewResponse;


public interface StoreReviewService {

    StoreReviewResponse list(Integer storeId);

    UserStoreReviewResponse get(Integer storeId);

    void add(StoreReviewAddRequest request);
}
