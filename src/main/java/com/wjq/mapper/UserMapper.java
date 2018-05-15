package com.wjq.mapper;

import com.wjq.model.User;
import org.springframework.stereotype.Component;

@Component("userMapper")
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    User selectByCertNo(String certNo);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}