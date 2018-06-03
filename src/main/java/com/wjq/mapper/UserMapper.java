package com.wjq.mapper;

import com.wjq.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("userMapper")
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    User selectByCertNo(String certNo);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List selectList(@Param(value = "userName") String userName,@Param(value = "cell") String cell,@Param(value = "certNo") String certNo);

    List selectUserChat();
}