package com.tgfc.tw.service;

import com.tgfc.tw.entity.model.request.store.StoreRequest;
import com.tgfc.tw.entity.model.request.store.StoreRequestV2_1;
import com.tgfc.tw.entity.model.request.store.StoreUpdateRequest;
import com.tgfc.tw.entity.model.response.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StoreInfoService {
    @Deprecated
    Page<StoreInfoResponse> getStoreAllOrByName(String keyword, List<Integer> tagIdList, int pageSize, int pageNumber);

    Page<NewStoreInfoResponse> getStoreList(String keyword, List<Integer> tagIdList, int pageSize, int pageNumber);

    @Deprecated
    StoreAddResponse addStore(StoreRequest store) throws Exception;

    StoreAddResponseV2_1 addStoreV2_1(StoreRequestV2_1 request);

    @Deprecated
    StoreGetOneResponse getStore(int id) throws Exception;

    StoreGetOneResponse getStoreV2_1(int id) throws Exception;

    @Deprecated
    void removeStore(int id) throws Exception;

    void newRemoveStore(int id) throws Exception;

    @Deprecated
    void updateStore(StoreUpdateRequest store) throws Exception;

    void newUpdateStore(StoreUpdateRequest store) throws Exception;

    void reviewStore(int storeId, int review) throws Exception;
}
