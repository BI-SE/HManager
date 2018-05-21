package com.wjq.mapper;

import com.wjq.model.Manager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "managerMapper")
public interface ManagerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Manager record);

    int insertSelective(Manager record);

    Manager selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Manager record);

    int updateByPrimaryKey(Manager record);

    Manager selectByUserName(String userName);

    List selectAll(Manager manager);
}