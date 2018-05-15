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

    @ApiOperation(value = "登记页面" ,notes = "")
    @RequestMapping(value = "signRoom.htm",method = RequestMethod.GET)
    public String signRoom(HttpServletRequest request,Model model){
        String roomId = request.getParameter("roomId");

        Room room =roomMapper.selectByRoomId(roomId);

        model.addAttribute("room",room);
        return "/signRoom";
    }

    @ApiOperation(value = "登记" ,notes = "")
    @ApiImplicitParam(name="room",value = "用户实体" ,required = true,dataType = "Room")
    @RequestMapping(value = "signRoom.do",method = RequestMethod.POST)
    @ResponseBody
    public Object signRoom(HttpServletRequest request,@ModelAttribute User userModel, Model model){

        String roomId = request.getParameter("roomId");
        String flag = request.getParameter("flag");

        User user = userMapper.selectByCertNo(userModel.getCertNo());

        //user为空则新增用户
        if(null==user){
            userModel.setGmtCreate(new Date());
            userModel.setGmtModified(new Date());
            userModel.setUserId(System.currentTimeMillis()+"");
            userMapper.insert(userModel);
        }

        //插入订单
        RoomOrder roomOrder = new RoomOrder();
        roomOrder.setActiveId("");
        roomOrder.setDepositAmount(new BigDecimal(0));
        roomOrder.setGmtCreate(new Date());
        roomOrder.setGmtModified(new Date());
        roomOrder.setMemo("");
        roomOrder.setOrderNo(System.currentTimeMillis()+"or");
        roomOrder.setStatus("0");
        roomOrder.setRoomId(roomId);
        roomOrder.setUserId(userModel.getUserId());

        roomOrderMapper.insert(roomOrder);

        if(null!=flag&&flag.equals("1")){
            roomMapper.updateByRoomId(roomId);
            roomOrderMapper.updateStatusByRoomId(roomId);
        }

        return "success";
    }

}
