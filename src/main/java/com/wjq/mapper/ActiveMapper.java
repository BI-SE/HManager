package com.wjq.mapper;

import com.wjq.model.Active;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("/activeMapper")
public interface ActiveMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Active record);

    int insertSelective(Active record);

    Active selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Active record);

    int updateByPrimaryKey(Active record);

    Active selectByActiveId(String activeId);

    List selectList(Active active);
}