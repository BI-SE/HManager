package com.wjq.controller;

import com.wjq.mapper.ActiveMapper;
import com.wjq.mapper.LogMapper;
import com.wjq.model.Active;
import com.wjq.model.Log;
import com.wjq.model.Manager;
import com.wjq.model.Result;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by deior on 2018/5/21.
 */
@Controller
@RequestMapping("/active")
public class ActiveController {

    @Autowired
    private ActiveMapper activeMapper;

    @Autowired
    private LogMapper logMapper;

    @ApiOperation(value = "/活动列表",notes = "")
    @RequestMapping(value = "/activeList.htm")
    public String activeList(HttpServletRequest request, Model model){

        String activeName = request.getParameter("activeName");
        String activeType = request.getParameter("activeType");

        Active active = new Active();
        active.setActiveName(activeName);
        active.setActiveType(activeType);

        List activeList =  activeMapper.selectList(active);

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        model.addAttribute("manager",managerDO);

        model.addAttribute("activeList",activeList);

        model.addAttribute("activeName",activeName);
        model.addAttribute("activeType",activeType);

        if(null==managerDO||"".equals(managerDO.getLevel())){
            return "/login";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log log = new Log();
        log.setContent(managerDO.getUserName()+"于"+dateFormat.format(new Date())+"进入活动页面");
        logMapper.insert(log);
        return "/active";
    }


    @ApiOperation(value = "新增/修改活动", notes = "")
    @RequestMapping(value = "editActive.htm")
    public String addRoom(HttpServletRequest request, Model model) {
        String activeId = request.getParameter("activeId");
        String activeFlag = "-1";
        Active active = new Active();

        if(null!=activeId&&!"".equals(activeId)){
             active =    activeMapper.selectByActiveId(activeId);

            if(null!=active){
                activeFlag = "1";
            }


        }

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        model.addAttribute("manager",managerDO);

        if(null==managerDO||"".equals(managerDO.getLevel())){
            return "/login";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log log = new Log();

        if(null!=activeId&&!"".equals(activeId)){

            log.setContent(managerDO.getUserName()+"于"+dateFormat.format(new Date())+"进入修改活动页面");

        }else{
            log.setContent(managerDO.getUserName()+"于"+dateFormat.format(new Date())+"进入新增活动页面");
        }

        logMapper.insert(log);

        model.addAttribute("active",active);
        model.addAttribute("activeFlag",activeFlag);

        return "/editActive";
    }

    @ApiOperation(value = "新增", notes = "")
    @ApiImplicitParam(name = "active", value = "活动实体", required = true, dataType = "Active")
    @RequestMapping(value = "/addActive.do", method = RequestMethod.POST)
    @ResponseBody
    public Object addActive(HttpServletRequest request, Model model) {

        String activeId = request.getParameter("activeId");
        String activeName = request.getParameter("activeName");
        String activeType = request.getParameter("activeType");
        String activePrice = request.getParameter("activePrice");
        String activeStartDate = request.getParameter("activeStartDate");
        String activeEndDate = request.getParameter("activeEndDate");



        if(activeName==null||"".equals(activeName)){
            return  new Result(false,"活动名称不能为空");
        }

        if(activeType==null||"".equals(activeType)){

            return  new Result(false,"活动类型不能为空");
        }

        if(activePrice==null||"".equals(activePrice)){

            return  new Result(false,"活动价格不能为空");
        }

        if(activeStartDate==null||"".equals(activeStartDate)){

            return  new Result(false,"开始时间不能为空");
        }

        if(activeEndDate==null||"".equals(activeEndDate)){

            return  new Result(false,"结束时间不能为空");
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date startDate = null;
        Date endDate = null;
        try {
             startDate =df.parse(activeStartDate);
             endDate =df.parse(activeEndDate);
        }catch (Exception e){

            return  new Result(false,"时间格式转化失败");
        }

        Active active = new Active();
        active.setActiveEndDate(endDate);
        active.setActiveId("ac"+System.currentTimeMillis());
        active.setActiveName(activeName);
        active.setActivePrice(new BigDecimal(activePrice));
        active.setActiveStartDate(startDate);
        active.setActiveType(activeType);
        active.setGmtCreate(new Date());
        active.setGmtModified(new Date());

        if(null!=activeId&&!"".equals(activeId)){
            active.setActiveId(activeId);
            activeMapper.updateByActiveId(active);
        }else{
            activeMapper.insert(active);
        }

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log log = new Log();
        log.setContent(managerDO.getUserName()+"于"+dateFormat.format(new Date())+"新增"+activeName+"活动");
        logMapper.insert(log);

        return  new Result(true,"成功");
    }

    @ApiOperation(value = "取消活动", notes = "")
    @RequestMapping(value = "quitActive.do", method = RequestMethod.POST)
    @ResponseBody
    public Object quitActive(HttpServletRequest request,Model model) {

        String activeId = request.getParameter("activeId");

        if(activeId==null||"".equals(activeId)){

            return  new Result(false,"活动id不能为空");
        }

        Active active = new  Active();
        active.setActiveEndDate(activeMapper.selectByActiveId(activeId).getActiveStartDate());
        active.setActiveId(activeId);
        activeMapper.updateDate(active);

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log log = new Log();
        log.setContent(managerDO.getUserName()+"于"+dateFormat.format(new Date())+"取消"+active.getActiveName()+"活动");
        logMapper.insert(log);

        return  new Result(true,"成功");
    }




}
