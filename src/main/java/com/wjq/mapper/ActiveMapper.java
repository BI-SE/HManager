package com.wjq.mapper;

import com.wjq.model.Active;

public interface ActiveMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Active record);

    int insertSelective(Active record);

    Active selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Active record);

    int updateByPrimaryKey(Active record);
}