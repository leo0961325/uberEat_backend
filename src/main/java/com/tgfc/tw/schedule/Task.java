package com.tgfc.tw.schedule;

import com.tgfc.tw.entity.model.po.Group;
import com.tgfc.tw.entity.repository.StoreRepository;
import com.tgfc.tw.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Task {

    @Autowired
    PayService payService;

    @Scheduled(fixedRate = 60000)
    public void checkPayment() {
        payService.checkPayments();
    }
}
