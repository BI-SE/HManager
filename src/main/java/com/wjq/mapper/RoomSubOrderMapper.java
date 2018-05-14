package com.wjq.mapper;

import com.wjq.model.RoomSubOrder;

public interface RoomSubOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RoomSubOrder record);

    int insertSelective(RoomSubOrder record);

    RoomSubOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoomSubOrder record);

    int updateByPrimaryKey(RoomSubOrder record);
}