package com.wjq.controller;

import com.wjq.mapper.*;
import com.wjq.model.*;
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
import java.text.SimpleDateFormat;
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

    @Autowired
    private RoomSubOrderMapper roomSubOrderMapper;

    @Autowired
    private ActiveMapper activeMapper;

    @Autowired
    private RoomConsumablesMapper roomConsumablesMapper;

    @Autowired
    private LogMapper logMapper;

    @ApiOperation(value = "登记页面", notes = "")
    @RequestMapping(value = "signRoom.htm", method = RequestMethod.GET)
    public String signRoom(HttpServletRequest request, Model model) {
        String roomId = request.getParameter("roomId");

        Room room = roomMapper.selectByRoomId(roomId);

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        model.addAttribute("manager",managerDO);

        model.addAttribute("room", room);


        if(null==managerDO||"".equals(managerDO.getLevel())){
            return "/login";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log log = new Log();
        log.setContent(managerDO.getUserName()+"于"+dateFormat.format(new Date())+"进入登记页面");
        logMapper.insert(log);

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
        //插入订单
        RoomOrder roomOrder = new RoomOrder();
        Room room = roomMapper.selectByRoomId(roomId);

        if("1".equals(room.getIsActive())){
            Active active =   activeMapper.selectByActiveId(room.getActiveId());
            if(active.getActiveEndDate().getTime()>=System.currentTimeMillis()){
                room.setPrice(active.getActivePrice());
                roomOrder.setActiveId(active.getActiveId());
            }
        }

        roomOrder.setDepositAmount(room.getPrice());
        roomOrder.setGmtCreate(new Date());
        roomOrder.setGmtModified(new Date());
        roomOrder.setMemo("");
        roomOrder.setOrderNo( "or"+System.currentTimeMillis() );
        roomOrder.setStatus("1");
        roomOrder.setRoomId(roomId);
        roomOrder.setUserId(userModel.getUserId());

        //插入子订单
        RoomSubOrder roomSubOrder = new RoomSubOrder();
        roomSubOrder.setAmount(room.getPrice());
        roomSubOrder.setGmtCreate(new Date());
        roomSubOrder.setGmtModified(new Date());
        roomSubOrder.setOrderId(roomOrder.getOrderNo());
        roomSubOrder.setPayStatus("0");
        roomSubOrder.setSubOrderId("sub"+System.currentTimeMillis());
        roomSubOrder.setType("0");
        roomSubOrderMapper.insert(roomSubOrder);

        roomOrderMapper.insert(roomOrder);

        roomConsumablesMapper.updateStatusByRoomId("1",roomId);

        if (null != flag && flag.equals("1")) {
            roomMapper.updateByRoomId(roomId);
            roomOrderMapper.updateStatusByRoomId(roomId);
        }

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log log = new Log();
        log.setContent(managerDO.getUserName()+"于"+dateFormat.format(new Date())+"登记"+room.getRoomName()+",用户为"+user.getName());
        logMapper.insert(log);

        return "success";
    }

    @ApiOperation(value = "房间列表", notes = "")
    @RequestMapping(value = "roomList.htm")
    public String roomList(HttpServletRequest request, Model model) {

        String roomName = request.getParameter("roomName");
        String roomType = request.getParameter("roomType");
        String status = request.getParameter("status");

        List<Room> rooms = roomMapper.selectAll(status, roomType, roomName);

        for(Room room:rooms){
            if("1".equals(room.getIsActive())){
              Active active =   activeMapper.selectByActiveId(room.getActiveId());
                room.setMemo(active.getActiveName());
              if(active.getActiveEndDate().getTime()>=System.currentTimeMillis()){
                  room.setPrice(active.getActivePrice());
              }
            }
        }

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        model.addAttribute("manager",managerDO);


        model.addAttribute("rooms", rooms);
        model.addAttribute("roomName", roomName);
        model.addAttribute("roomType", roomType);
        model.addAttribute("status", status);


        if(null==managerDO||"".equals(managerDO.getLevel())){
            return "/login";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log log = new Log();
        log.setContent(managerDO.getUserName()+"于"+dateFormat.format(new Date())+"进入房间列表");
        logMapper.insert(log);

        return "/room";
    }

    @ApiOperation(value = "新增房间", notes = "")
    @RequestMapping(value = "editRoom.htm")
    public String addRoom(HttpServletRequest request, Model model) {

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        model.addAttribute("manager",managerDO);

        if(null==managerDO||"".equals(managerDO.getLevel())){
            return "/login";
        }

      List activeList =  activeMapper.selectList(new Active());

        model.addAttribute("activeList",activeList);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log log = new Log();
        log.setContent(managerDO.getUserName()+"于"+dateFormat.format(new Date())+"进入新增房间页面");
        logMapper.insert(log);

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
        String activeId = request.getParameter("activeId");

        if(roomName==null||"".equals(roomName)){

            return  new Result(false,"用户名不能为空");
        }

        if(roomType==null||"".equals(roomType)){

            return  new Result(false,"用户类型不能为空");
        }

        if(price==null||"".equals(price)){

            return  new Result(false,"价格不能为空");
        }

        Room room = new Room();
        room.setRoomName(roomName);
        room.setRoomType(roomType);
        room.setRoomId("room"+System.currentTimeMillis());
        room.setPrice(new BigDecimal(price));
        room.setGmtCreate(new Date());
        room.setGmtModified(new Date());
        room.setStatus("0");
        room.setActiveId(activeId);
        room.setIsActive("1");
        roomMapper.insert(room);

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log log = new Log();
        log.setContent(managerDO.getUserName()+"于"+dateFormat.format(new Date())+"新增"+roomName+"房间");
        logMapper.insert(log);

        return  new Result(true,"成功");
    }


    @ApiOperation(value = "取消活动", notes = "")
    @RequestMapping(value = "quitActive.do", method = RequestMethod.POST)
    @ResponseBody
    public Object quitActive(HttpServletRequest request,Model model) {

        String roomId = request.getParameter("roomId");

        if(roomId==null||"".equals(roomId)){

            return  new Result(false,"房间id不能为空");
        }

        roomMapper.updateActiveByRoomId(roomId);

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log log = new Log();
        log.setContent(managerDO.getUserName()+"于"+dateFormat.format(new Date())+"取消"+roomId+"房间活动");
        logMapper.insert(log);

        return  new Result(true,"成功");
    }

}
