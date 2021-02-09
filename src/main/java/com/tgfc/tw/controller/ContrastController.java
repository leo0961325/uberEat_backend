package com.tgfc.tw.controller;


import com.tgfc.tw.service.ContrastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/contrast")
public class ContrastController {

    @Autowired
    ContrastService contrastService;

    @GetMapping("v2.1/payType")
    public Map<Integer, String> getPayType() {

        return contrastService.getPayType();
    }
}

