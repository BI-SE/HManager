package com.wjq.mapper;

import com.wjq.model.RoomOrder;

public interface RoomOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RoomOrder record);

    int insertSelective(RoomOrder record);

    RoomOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoomOrder record);

    int updateByPrimaryKey(RoomOrder record);
}