package com.wjq.controller;

import com.wjq.mapper.LogMapper;
import com.wjq.mapper.RoomMapper;
import com.wjq.mapper.RoomOrderMapper;
import com.wjq.model.Log;
import com.wjq.model.Manager;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 订单控制类
 * Created by deior on 2018/5/20.
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Resource
    private RoomOrderMapper roomOrderMapper;

    @Resource
    private RoomMapper roomMapper;

    @Autowired
    private LogMapper logMapper;

    @ApiOperation(value = "订单列表页面",notes = "")
    @RequestMapping("/orderList.htm")
    public String orderList(HttpServletRequest request,Model model){

        String userName = request.getParameter("userName");
        String roomName = request.getParameter("roomName");


        List orderList = roomOrderMapper.selectAll(userName,roomName);

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        model.addAttribute("manager",managerDO);

        model.addAttribute("orderList",orderList);

        model.addAttribute("userName",userName);
        model.addAttribute("roomName",roomName);


        if(null==managerDO||"".equals(managerDO.getLevel())){
            return "/login";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log log = new Log();
        log.setContent(managerDO.getUserName()+"于"+dateFormat.format(new Date())+"进入订单列表页面");
        logMapper.insert(log);

        return  "/order";
    }

    @ApiOperation(value = "退房",notes = "")
    @RequestMapping("/quitRoom.do")
    @ResponseBody
    public String quitRoom(HttpServletRequest request){

        String orderNo = request.getParameter("orderNo");
        String roomId = request.getParameter("roomId");

        if(orderNo==null||"".equals(orderNo)){
            throw new  RuntimeException("订单号不能为空");
        }

        if(roomId==null||"".equals(roomId)){
            throw new  RuntimeException("房间号不能为空");
        }


        roomMapper.updateStateByRoomId(roomId,"0");
        roomOrderMapper.updateStatusByOrderNo(orderNo,"2");

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log log = new Log();
        log.setContent(managerDO.getUserName()+"于"+dateFormat.format(new Date())+"退"+roomId+"房间");
        logMapper.insert(log);

        return  "successr";
    }
}
