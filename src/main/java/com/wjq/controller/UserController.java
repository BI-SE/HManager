package com.wjq.controller;

import com.wjq.mapper.ManagerMapper;
import com.wjq.mapper.RoomMapper;
import com.wjq.model.Manager;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @Autowired
    private RoomMapper roomMapper;

    @ApiOperation(value = "进入登录页面",notes = "")
    @RequestMapping(value = "/login.htm",method =RequestMethod.GET)
    public String login(Model model){
        return "/login";
    }

    @ApiOperation(value = "登录" ,notes = "")
    @ApiImplicitParam(name = "manager",value = "用户实体对象",required =true,dataType = "Manager")
    @RequestMapping(value ="/index.htm",method = RequestMethod.GET)
    public String index(@ModelAttribute Manager manager, Model model){

        List roomList = roomMapper.selectAll("0");

        model.addAttribute("roomList",roomList);


        return "/index";
    }

}
