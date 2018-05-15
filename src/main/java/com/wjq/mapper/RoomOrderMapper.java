package com.wjq.mapper;

import com.wjq.model.RoomOrder;
import org.springframework.stereotype.Component;

@Component("roomOrderMapper")
public interface RoomOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RoomOrder record);

    int insertSelective(RoomOrder record);

    RoomOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoomOrder record);

    int updateStatusByRoomId(String roomId);

    int updateByPrimaryKey(RoomOrder record);
}