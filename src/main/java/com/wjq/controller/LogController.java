package com.wjq.controller;

import com.wjq.mapper.LogMapper;
import com.wjq.model.Active;
import com.wjq.model.Log;
import com.wjq.model.Manager;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by deior on 2018/5/23.
 */
@Controller
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogMapper logMapper;

    @ApiOperation(value = "/日志列表",notes = "")
    @RequestMapping(value = "/log.htm")
    public String activeList(HttpServletRequest request, Model model){

        List logs =  logMapper.selecList();

        Manager managerDO = (Manager) request.getSession().getAttribute("manager");
        model.addAttribute("manager",managerDO);

        model.addAttribute("logs",logs);

        if(null==managerDO||"".equals(managerDO.getLevel())){
            return "/login";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log log = new Log();
        log.setContent(managerDO.getUserName()+"于"+dateFormat.format(new Date())+"进入日志列表页面");
        logMapper.insert(log);
        return "/log";
    }
}
