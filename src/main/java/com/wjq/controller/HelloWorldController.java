package com.wjq.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by deior on 2018/5/14.
 */
@RestController
public class HelloWorldController {

    @RequestMapping("/hello")
    public String index(){
        return  "helloWorld";
    }

}
