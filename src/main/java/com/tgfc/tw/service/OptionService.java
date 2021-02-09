package com.tgfc.tw.service;

import com.tgfc.tw.entity.model.po.Option;
import com.tgfc.tw.entity.model.request.OptionAddRequest;
import com.tgfc.tw.entity.model.request.OptionUpdateRequest;
import com.tgfc.tw.entity.model.response.OptionCountResponse;
import com.tgfc.tw.entity.model.response.OptionResponse;

import java.util.List;

public interface OptionService {

    Option addOption(String name, int price, int productId) throws Exception;

    @Deprecated
    void addOneOption(OptionAddRequest request) throws Exception;

    @Deprecated
    void managerAddOneOption(OptionAddRequest request) throws Exception;

    void deleteOption(int id) throws Exception;

    void updateOption(OptionUpdateRequest request) throws Exception;

    List<OptionResponse> getOptionByOrder(int productId);

    OptionResponse getOptionById(int optionId) throws Exception;

    List<OptionCountResponse> getOptionByGroup(int groupId, int productId) throws Exception;
}
