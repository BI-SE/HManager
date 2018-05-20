package com.wjq.controller;

import com.wjq.mapper.RoomMapper;
import com.wjq.mapper.RoomOrderMapper;
import com.wjq.mapper.UserMapper;
import com.wjq.model.Room;
import com.wjq.model.RoomOrder;
import com.wjq.model.User;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 房间控制类
 * Created by deior on 2018/5/15.
 */
@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoomOrderMapper roomOrderMapper;

    @ApiOperation(value = "登记页面", notes = "")
    @RequestMapping(value = "signRoom.htm", method = RequestMethod.GET)
    public String signRoom(HttpServletRequest request, Model model) {
        String roomId = request.getParameter("roomId");

        Room room = roomMapper.selectByRoomId(roomId);

        model.addAttribute("room", room);
        return "/signRoom";
    }

    @ApiOperation(value = "登记", notes = "")
    @ApiImplicitParam(name = "room", value = "用户实体", required = true, dataType = "Room")
    @RequestMapping(value = "signRoom.do", method = RequestMethod.POST)
    @ResponseBody
    public Object signRoom(HttpServletRequest request, @ModelAttribute User userModel, Model model) {

        String roomId = request.getParameter("roomId");
        String flag = request.getParameter("flag");

        User user = userMapper.selectByCertNo(userModel.getCertNo());

        //user为空则新增用户
        if (null == user) {
            userModel.setGmtCreate(new Date());
            userModel.setGmtModified(new Date());
            userModel.setUserId(System.currentTimeMillis() + "");
            userMapper.insert(userModel);
        }

        Room room = roomMapper.selectByRoomId(roomId);

        //插入订单
        RoomOrder roomOrder = new RoomOrder();
        roomOrder.setActiveId("");
        roomOrder.setDepositAmount(room.getPrice());
        roomOrder.setGmtCreate(new Date());
        roomOrder.setGmtModified(new Date());
        roomOrder.setMemo("");
        roomOrder.setOrderNo(System.currentTimeMillis() + "or");
        roomOrder.setStatus("1");
        roomOrder.setRoomId(roomId);
        roomOrder.setUserId(userModel.getUserId());

        roomOrderMapper.insert(roomOrder);

        if (null != flag && flag.equals("1")) {
            roomMapper.updateByRoomId(roomId);
            roomOrderMapper.updateStatusByRoomId(roomId);
        }

        return "success";
    }

    @ApiOperation(value = "房间列表", notes = "")
    @RequestMapping(value = "roomList.htm")
    public String roomList(HttpServletRequest request, Model model) {

        String roomName = request.getParameter("roomName");
        String roomType = request.getParameter("roomType");
        String status = request.getParameter("status");

        List rooms = roomMapper.selectAll(status, roomType, roomName);


        model.addAttribute("rooms", rooms);
        model.addAttribute("roomName", roomName);
        model.addAttribute("roomType", roomType);
        model.addAttribute("status", status);

        return "/room";
    }

    @ApiOperation(value = "新增房间", notes = "")
    @RequestMapping(value = "editRoom.htm")
    public String addRoom(HttpServletRequest request, Model model) {


        return "/editRoom";
    }


    @ApiOperation(value = "新增", notes = "")
    @ApiImplicitParam(name = "room", value = "用户实体", required = true, dataType = "Room")
    @RequestMapping(value = "addRoom.do", method = RequestMethod.POST)
    @ResponseBody
    public Object addRoom(HttpServletRequest request, @ModelAttribute User userModel, Model model) {

        String roomName = request.getParameter("roomName");
        String roomType = request.getParameter("roomType");
        String price = request.getParameter("price");

        if(roomName==null||"".equals(roomName)){
            throw new  RuntimeException("用户名不能为空");
        }

        if(roomType==null||"".equals(roomType)){
            throw new  RuntimeException("用户类型不能为空");
        }

        if(price==null||"".equals(price)){
            throw new  RuntimeException("价格不能为空");
        }

        Room room = new Room();
        room.setRoomName(roomName);
        room.setRoomType(roomType);
        room.setRoomId("room"+System.currentTimeMillis());
        room.setPrice(new BigDecimal(price));
        room.setGmtCreate(new Date());
        room.setGmtModified(new Date());
        room.setStatus("0");
        roomMapper.insert(room);

        return "success";
    }

}
