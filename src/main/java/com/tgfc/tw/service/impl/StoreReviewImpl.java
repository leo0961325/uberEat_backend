package com.tgfc.tw.service.impl;

import com.tgfc.tw.entity.model.po.Store;
import com.tgfc.tw.entity.model.po.StoreReview;
import com.tgfc.tw.entity.model.request.StoreReviewAddRequest;
import com.tgfc.tw.entity.model.response.RankMessageResponse;
import com.tgfc.tw.entity.model.response.StoreGetOneResponse;
import com.tgfc.tw.entity.model.response.StoreReviewResponse;
import com.tgfc.tw.entity.model.response.UserStoreReviewResponse;
import com.tgfc.tw.entity.repository.StoreRepository;
import com.tgfc.tw.entity.repository.StoreReviewRepository;
import com.tgfc.tw.entity.repository.UserRepository;
import com.tgfc.tw.security.ContextHolderHandler;
import com.tgfc.tw.service.StoreReviewService;
import com.tgfc.tw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreReviewImpl implements StoreReviewService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StoreReviewRepository storeReviewRepository;

    @Autowired
    StoreRepository storeRepository;

    @Override
    public StoreReviewResponse list(Integer storeId) {

        StoreReviewResponse storeReviewResponse = new StoreReviewResponse();

        List<RankMessageResponse> allMessage = storeReviewRepository.getAllByStoreId(storeId);

        if (allMessage.size() != 0) {
            storeReviewResponse.setTotalMessage(allMessage.stream().filter(r -> !(r.getMessage() == null || r.getMessage().equals(""))).count());
            storeReviewResponse.setTotalUsers(allMessage.size());
            storeReviewResponse.setRankList(allMessage.stream().filter(r -> !(r.getMessage() == null || r.getMessage().equals(""))).collect(Collectors.toList()));
        } else {
            storeReviewResponse.setTotalUsers(0);
            storeReviewResponse.setRankList(new ArrayList<>());
            storeReviewResponse.setTotalMessage((long) 0);
        }
        return storeReviewResponse;
    }

    @Override
    public UserStoreReviewResponse get(Integer storeId) {

        UserStoreReviewResponse user = storeReviewRepository.getByUserId(storeId, ContextHolderHandler.getId());
        if (user != null)
            return user;
        else {
            UserStoreReviewResponse result = new UserStoreReviewResponse();
            StoreGetOneResponse store = storeRepository.getStore(storeId);

            if (store == null)
                throw new IllegalArgumentException("store is not found!");

            result.setStoreId(store.getId());
            result.setStoreName(store.getStoreName());
            return result;
        }
    }

    @Override
    public void add(StoreReviewAddRequest request) {
        StoreReview storeReview = new StoreReview();
        Store store = storeRepository.getById(request.getStoreId());

        if (store == null)
            throw new IllegalArgumentException("Store not found!");

        if (request.getStoreReviewId() != null)
            storeReview.setId(request.getStoreReviewId());

        if(request.getRank() < 1)
            throw new IllegalArgumentException("評分必須大於一顆星!");

        storeReview.setStore(store);
        storeReview.setReview(request.getRank());
        storeReview.setDate(new Timestamp(System.currentTimeMillis()));
        storeReview.setMessage(request.getMessage());
        storeReview.setUser(userRepository.getUserById(ContextHolderHandler.getId()));

        storeReviewRepository.save(storeReview);
    }
}
