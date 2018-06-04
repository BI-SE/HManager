package com.wjq.controller;

import com.wjq.mapper.LogMapper;
import com.wjq.mapper.RoomMapper;
import com.wjq.mapper.RoomOrderMapper;
import com.wjq.mapper.RoomSubOrderMapper;
import com.wjq.model.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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

    @Autowired
    private RoomSubOrderMapper roomSubOrderMapper;

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
    public Object quitRoom(HttpServletRequest request){

        String orderNo = request.getParameter("orderNo");
        String roomId = request.getParameter("roomId");

        if(orderNo==null||"".equals(orderNo)){

            return  new Result(false,"订单号不能为空");
        }

        if(roomId==null||"".equals(roomId)){

            return  new Result(false,"房间号不能为空");
        }


        roomMapper.updateStateByRoomId(roomId,"0");
        roomOrderMapper.updateStatusByOrderNo(orderNo,"2");
        roomSubOrderMapper.updateByOrderId(orderNo);
        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log log = new Log();
        log.setContent(managerDO.getUserName()+"于"+dateFormat.format(new Date())+"退"+roomId+"房间");
        logMapper.insert(log);

        return  new Result(true,"成功");
    }

    @ApiOperation(value = "订单列表页面",notes = "")
    @RequestMapping("/subOrderList.htm")
    public String subOrderList(HttpServletRequest request,Model model){

        String orderId = request.getParameter("orderId");
        RoomSubOrder roomSubOrder = new RoomSubOrder();
        roomSubOrder.setOrderId(orderId);

        List orderList = roomSubOrderMapper.selectByorderId(roomSubOrder);

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        model.addAttribute("manager",managerDO);

        model.addAttribute("orderList",orderList);

        model.addAttribute("orderId",orderId);

        if(null==managerDO||"".equals(managerDO.getLevel())){
            return "/login";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log log = new Log();
        log.setContent(managerDO.getUserName()+"于"+dateFormat.format(new Date())+"进入子订单列表页面");
        logMapper.insert(log);

        return  "/subOrder";
    }

    @ApiOperation(value = "结账", notes = "")
    @RequestMapping(value = "quitRoom.htm")
    public String quitRoom(HttpServletRequest request, Model model) {

        String orderNo = request.getParameter("orderNo");

        String roomId = request.getParameter("roomId");

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        model.addAttribute("manager",managerDO);

        if(null==managerDO||"".equals(managerDO.getLevel())){
            return "/login";
        }

        RoomSubOrder roomSubOrder = new RoomSubOrder();
        roomSubOrder.setOrderId(orderNo);

        List<RoomSubOrder> orderList = roomSubOrderMapper.selectByorderId(roomSubOrder);

        model.addAttribute("orderList",orderList);

        BigDecimal totalAmount = new BigDecimal(0);
        for(RoomSubOrder roomSubOrder1:orderList){
            totalAmount = totalAmount.add(roomSubOrder1.getAmount());
        }
        model.addAttribute("totalAmount",totalAmount);

        Room room = roomMapper.selectByRoomId(roomId);
        model.addAttribute("room",room);

        model.addAttribute("orderNo",orderNo);
        model.addAttribute("roomId",roomId);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log log = new Log();
        log.setContent(managerDO.getUserName()+"于"+dateFormat.format(new Date())+"进入结账页面");
        logMapper.insert(log);

        return "/quitRoom";
    }


}
