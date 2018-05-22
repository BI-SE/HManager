package com.wjq.controller;

import com.wjq.mapper.ManagerMapper;
import com.wjq.mapper.RoomMapper;
import com.wjq.mapper.UserMapper;
import com.wjq.model.Manager;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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

    @Autowired
    private UserMapper userMapper;

    @ApiOperation(value = "进入登录页面",notes = "")
    @RequestMapping(value = "/login.htm",method =RequestMethod.GET)
    public String login(Model model){
        return "/login";
    }

    @ApiOperation(value = "登录")
    @ApiImplicitParam(name = "manager",value = "用户实体对象",required =true,dataType = "Manager")
    @RequestMapping(value ="/login.do",method = RequestMethod.POST)
    @ResponseBody
    public Object login(HttpServletRequest request,@ModelAttribute Manager manager, Model model){
        if(null==manager){
            throw  new RuntimeException("请输入账号密码");
        }

        if(null==manager.getUserName()||"".equals(manager.getUserPassword())){
            throw  new RuntimeException("请输入账号密码");
        }

        Manager managerDO =    managerMapper.selectByUserName(manager.getUserName());

        if(null==managerDO){
            throw new RuntimeException("用户不存在，请确认账号!");
        }

        if(!managerDO.getUserPassword().equals(manager.getUserPassword())){
            throw  new RuntimeException("密码错误,请重新输入!");
        }
        model.addAttribute("manager",managerDO);

        request.getSession().setAttribute("manager",managerDO);

        return "success";

    }

    @ApiOperation(value = "主页面" ,notes = "")
    @ApiImplicitParam(name = "manager",value = "用户实体对象",required =true,dataType = "Manager")
    @RequestMapping(value ="/index.htm",method = RequestMethod.GET)
    public String index(HttpServletRequest request,@ModelAttribute Manager manager, Model model){

        List roomList = roomMapper.selectAll("0",null,null);

        model.addAttribute("roomList",roomList);

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");

        if(null==managerDO||"".equals(managerDO.getLevel())){
            return "/login";
        }
        model.addAttribute("manager",managerDO);

        return "/index";
    }

    @ApiOperation(value = "用户列表" ,notes = "")
    @RequestMapping(value ="/userList.htm",method = RequestMethod.GET)
    public String userList(HttpServletRequest request, Model model){

        String userName = request.getParameter("userName");
        String cell = request.getParameter("cell");
        String certNo = request.getParameter("certNo");

       List userList = userMapper.selectList(userName,cell,certNo);
       Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        model.addAttribute("manager",managerDO);

        model.addAttribute("userList",userList);

        model.addAttribute("userName",userName);
        model.addAttribute("cell",cell);
        model.addAttribute("certNo",certNo);


        if(null==managerDO||"".equals(managerDO.getLevel())){
            return "/login";
        }

        return "/user";
    }

    @ApiOperation(value = "进入后台管理用户",notes = "")
    @RequestMapping(value = "/managerList.htm",method =RequestMethod.GET)
    public String managerList(HttpServletRequest request,Model model){

        String userName = request.getParameter("userName");

        Manager manager = new Manager();
        manager.setUserName(userName);
      List managerList =   managerMapper.selectAll(manager);

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        model.addAttribute("manager",managerDO);
        model.addAttribute("managerList",managerList);

        model.addAttribute("userName",userName);


        if(null==managerDO||"".equals(managerDO.getLevel())){
            return "/login";
        }

        return "/manager";
    }

    @ApiOperation(value = "新增后台用户", notes = "")
    @RequestMapping(value = "editManager.htm")
    public String addRoom(HttpServletRequest request, Model model) {


        return "/editManager";
    }


    @ApiOperation(value = "新增", notes = "")
    @ApiImplicitParam(name = "manager", value = "活动实体", required = true, dataType = "Manager")
    @RequestMapping(value = "/addManager.do", method = RequestMethod.POST)
    @ResponseBody
    public Object addActive(HttpServletRequest request, Model model) {

        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        String level = request.getParameter("level");

        if(userName==null||"".equals(userName)){
            throw new  RuntimeException("后台用户名称不能为空");
        }

        if(userPassword==null||"".equals(userPassword)){
            throw new  RuntimeException("密码不能为空");
        }

        if(level==null||"".equals(level)){
            throw new  RuntimeException("权限等级不能为空");
        }

        Manager manager = new Manager();
        manager.setUserName(userName);
        manager.setGmtCreate(new Date());
        manager.setGmtModified(new Date());
        manager.setLevel(Integer.parseInt(level));
        manager.setUserPassword(userPassword);

        managerMapper.insert(manager);

        return "success";
    }

}
