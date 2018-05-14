package com.wjq.mapper;

import com.wjq.model.RoomStatusChangeLog;

public interface RoomStatusChangeLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RoomStatusChangeLog record);

    int insertSelective(RoomStatusChangeLog record);

    RoomStatusChangeLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoomStatusChangeLog record);

    int updateByPrimaryKey(RoomStatusChangeLog record);
}