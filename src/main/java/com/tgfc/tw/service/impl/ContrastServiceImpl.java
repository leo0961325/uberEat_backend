package com.tgfc.tw.service.impl;

import com.tgfc.tw.entity.cenum.pay.PayTypeEnum;
import com.tgfc.tw.service.ContrastService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContrastServiceImpl implements ContrastService {
    @Override
    public Map<Integer, String> getPayType() {
        Map<Integer, String> enumList = new LinkedHashMap<>();
        enumList.put(PayTypeEnum.PAY_TYPE_CASH.getType(), PayTypeEnum.PAY_TYPE_CASH.getTrans());
        enumList.put(PayTypeEnum.PAY_TYPE_TRANSFER.getType(), PayTypeEnum.PAY_TYPE_TRANSFER.getTrans());
        enumList.put(PayTypeEnum.PAY_TYPE_LINE_PAY.getType(), PayTypeEnum.PAY_TYPE_LINE_PAY.getTrans());
        enumList.put(PayTypeEnum.PAY_TYPE_OTHER.getType(), PayTypeEnum.PAY_TYPE_OTHER.getTrans());
        return enumList;
    }
}
