package com.tgfc.tw.service;

import com.tgfc.tw.entity.model.request.OptionAddRequestV2_1;

public interface OrdService {

    void addOrderItem(int optionId, int groupId);

    void delOrderItem(int productId, int groupId);

    void managerAddOrder(int optionId, int groupId, int userId);

    void managerDeleteOrder(int productId, int groupId, int userId);

    void addOptionAndOrder(OptionAddRequestV2_1 request);

    void managerAddOptionAndOrder(OptionAddRequestV2_1 request);

    void deleteOption(int optionId, int groupId);

    void initPayRecord();
}
