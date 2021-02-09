package com.tgfc.tw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgfc.tw.entity.model.response.VersionResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class VersionController {


    @GetMapping("/version")
    public VersionResponse version() throws IOException {

        String s = IOUtils.toString(getClass().getResourceAsStream("/version.json"), "utf-8");
        return new ObjectMapper().readValue(s, VersionResponse.class);


    }
}