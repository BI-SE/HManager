package com.wjq.controller;

import com.wjq.mapper.ManagerMapper;
import com.wjq.model.Manager;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户控制类
 * Created by deior on 2018/5/14.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ManagerMapper managerMapper;

    @ApiOperation(value = "登录" ,notes = "")
    @ApiImplicitParam(name = "manager",value = "用户实体对象",required =true,dataType = "Manager")
    @RequestMapping(value ="/login.htm",method = RequestMethod.GET)
    public String login(@ModelAttribute Manager manager, Model model){

        Manager manager1 =  managerMapper.selectByPrimaryKey(1);

        model.addAttribute("hello","Hello, Spring Boodfgft!");
        model.addAttribute("manager1",manager1);


        return "/user/list2";
    }


    public void setManagerMapper(ManagerMapper managerMapper) {
        this.managerMapper = managerMapper;
    }
}
