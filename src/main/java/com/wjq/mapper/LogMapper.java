package com.wjq.mapper;

import com.wjq.model.Log;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("/logMapper")
public interface LogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Log record);

    int insertSelective(Log record);

    Log selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Log record);

    int updateByPrimaryKey(Log record);

    List selecList();
}