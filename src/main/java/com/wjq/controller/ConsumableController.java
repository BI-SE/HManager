package com.wjq.controller;

import com.wjq.mapper.RoomConsumablesMapper;
import com.wjq.mapper.RoomMapper;
import com.wjq.mapper.RoomOrderMapper;
import com.wjq.mapper.RoomSubOrderMapper;
import com.wjq.model.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 消耗品
 * Created by deior on 2018/5/21.
 */
@Controller
@RequestMapping("/consumable")
public class ConsumableController {

    @Autowired
    private RoomConsumablesMapper roomConsumablesMapper;

    @Autowired
    private RoomSubOrderMapper roomSubOrderMapper;

    @Autowired
    private RoomOrderMapper roomOrderMapper ;

    @Autowired
    private RoomMapper roomMapper;

    @ApiOperation(value = "消耗品列表",notes = "")
    @RequestMapping(value = "/consumableList.htm")
    public String consumableList(HttpServletRequest request, Model model){

        String roomName = request.getParameter("roomName");
        String name = request.getParameter("name");

        List consumableList = roomConsumablesMapper.selectList(roomName,name);

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        model.addAttribute("manager",managerDO);

        model.addAttribute("consumableList",consumableList);

        if(null==managerDO||"".equals(managerDO.getLevel())){
            return "/login";
        }

        return "/consumable";

    }
    @ApiOperation(value = "新增消耗品", notes = "")
    @RequestMapping(value = "editConsumable.htm")
    public String editConsumable(HttpServletRequest request, Model model) {


        return "/editConsumable";
    }

    @ApiOperation(value = "新增", notes = "")
    @ApiImplicitParam(name = "active", value = "活动实体", required = true, dataType = "Active")
    @RequestMapping(value = "addConsumable.do", method = RequestMethod.POST)
    @ResponseBody
    public Object addConsumable(HttpServletRequest request, Model model) {

        String roomId = request.getParameter("roomId");
        String name = request.getParameter("name");
        String price = request.getParameter("price");
        String status = request.getParameter("status");

        if(roomId==null||"".equals(roomId)){
            throw new  RuntimeException("房间id不能为空");
        }

        if(name==null||"".equals(name)){
            throw new  RuntimeException("名称不能为空");
        }

        if(price==null||"".equals(price)){
            throw new  RuntimeException("价格不能为空");
        }

        if(status==null||"".equals(status)){
            throw new  RuntimeException("状态不能为空");
        }

        RoomConsumables roomConsumables = new RoomConsumables();
        roomConsumables.setGmtCreate(new Date());
        roomConsumables.setGmtModified(new Date());
        roomConsumables.setName(name);
        roomConsumables.setPrice(new BigDecimal(price));
        roomConsumables.setRoomId(roomId);
        roomConsumables.setStatus(status);

        roomConsumablesMapper.insert(roomConsumables);

        return "success";
    }

    @ApiOperation(value = "使用", notes = "")
    @RequestMapping(value = "useConsumable.do", method = RequestMethod.POST)
    @ResponseBody
    public Object useConsumable(HttpServletRequest request, Model model) {

        String id = request.getParameter("id");


        if(id==null||"".equals(id)){
            throw new  RuntimeException("id不能为空");
        }

      RoomConsumables roomConsumables =  roomConsumablesMapper.selectByPrimaryKey(Long.parseLong(id));


    Room room =roomMapper.selectByRoomId(roomConsumables.getRoomId());

    if(null==room||"0".equals(room.getStatus())){
        throw  new RuntimeException("房间没有使用");
    }

    RoomOrder roomOrder =roomOrderMapper.selectBRoomId(roomConsumables.getRoomId());

        if(null==roomOrder){
            throw  new RuntimeException("房间没有使用");
        }

        //插入子订单
        RoomSubOrder roomSubOrder = new RoomSubOrder();
        roomSubOrder.setAmount(roomConsumables.getPrice());
        roomSubOrder.setGmtCreate(new Date());
        roomSubOrder.setGmtModified(new Date());
        roomSubOrder.setOrderId(roomOrder.getOrderNo());
        roomSubOrder.setPayStatus("0");
        roomSubOrder.setSubOrderId("sub"+System.currentTimeMillis());
        roomSubOrder.setType("0");
        roomSubOrderMapper.insert(roomSubOrder);

        roomConsumablesMapper.updateStatusByRoomId("2",roomConsumables.getRoomId());


        return "success";
    }



}
