package com.tgfc.tw.controller;

import com.tgfc.tw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @Autowired
    UserService userService;

    @GetMapping(value = "api/demo/hw")
    public String hw(){
        return "Hello World!!";
    }

    @GetMapping(value = "api/demo/hw2")
    public String hw2(){
        return "Hello World!!2";
    }

    @GetMapping(value = "api/demo/hw3")
    public String hw3(){
        return "Hello World!!3";
    }

    @GetMapping(value = "api/demo/mc")
    public String mc(){
        return "Merry Christmas !!!";
    }

    @GetMapping(value = "api/demo/leo")
    public String leoTest(){
        return "Editing Teach Meterial By Leo!!";
    }

    @GetMapping(value = "api/demo/JJ")
    public String JJTest(){
        return "Testing By JJTest3!!";
    }

    @GetMapping(value = "api/demo/Rex")
    public String RexTest(){
        return "Jenkins Auto deploy Testing By Rex";
    }

    @GetMapping(value = "api/demo/andrew")
    public  String andrewTest() {
        return "Jenkins Testing 123";
    }

    @GetMapping("api/demo/api401")
    public ResponseEntity api401(){
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
