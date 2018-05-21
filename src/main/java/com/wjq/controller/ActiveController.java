package com.wjq.controller;

import com.wjq.mapper.ActiveMapper;
import com.wjq.model.Active;
import com.wjq.model.Manager;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.omg.PortableInterceptor.ACTIVE;
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

    @ApiOperation(value = "/活动列表",notes = "")
    @RequestMapping(value = "/activeList.htm")
    public String activeList(HttpServletRequest request, Model model){

       List activeList =  activeMapper.selectList(null);

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        model.addAttribute("manager",managerDO);

        model.addAttribute("activeList",activeList);

        return "/active";
    }


    @ApiOperation(value = "新增活动", notes = "")
    @RequestMapping(value = "editActive.htm")
    public String addRoom(HttpServletRequest request, Model model) {


        return "/editActive";
    }

    @ApiOperation(value = "新增", notes = "")
    @ApiImplicitParam(name = "active", value = "活动实体", required = true, dataType = "Active")
    @RequestMapping(value = "addActive,do", method = RequestMethod.POST)
    @ResponseBody
    public Object addActive(HttpServletRequest request, Model model) {

        String activeName = request.getParameter("activeName");
        String activeType = request.getParameter("activeType");
        String activePrice = request.getParameter("activePrice");
        String activeStartDate = request.getParameter("activeStartDate");
        String activeEndDate = request.getParameter("activeEndDate");

        if(activeName==null||"".equals(activeName)){
            throw new  RuntimeException("活动名称不能为空");
        }

        if(activeType==null||"".equals(activeType)){
            throw new  RuntimeException("活动类型不能为空");
        }

        if(activePrice==null||"".equals(activePrice)){
            throw new  RuntimeException("活动价格不能为空");
        }

        if(activeStartDate==null||"".equals(activeStartDate)){
            throw new  RuntimeException("开始时间不能为空");
        }

        if(activeEndDate==null||"".equals(activeEndDate)){
            throw new  RuntimeException("结束时间不能为空");
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date startDate = null;
        Date endDate = null;
        try {
             startDate =df.parse(activeStartDate);
             endDate =df.parse(activeEndDate);
        }catch (Exception e){
            throw  new RuntimeException("时间格式转化失败");
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

        activeMapper.insert(active);

        return "success";
    }

    @ApiOperation(value = "取消活动", notes = "")
    @RequestMapping(value = "quitActive.do", method = RequestMethod.POST)
    @ResponseBody
    public Object quitActive(HttpServletRequest request,Model model) {

        String activeId = request.getParameter("activeId");

        if(activeId==null||"".equals(activeId)){
            throw new  RuntimeException("活动id不能为空");
        }

        Active active = new  Active();
        active.setActiveEndDate(activeMapper.selectByActiveId(activeId).getActiveStartDate());
        activeMapper.updateByPrimaryKeySelective(active);

        return "success";
    }




}
