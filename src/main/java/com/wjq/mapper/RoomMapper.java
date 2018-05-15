package com.wjq.mapper;

import com.wjq.model.Room;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("roomMapper")
public interface RoomMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Room record);

    int insertSelective(Room record);

    Room selectByPrimaryKey(Long id);

    Room selectByRoomId(String roomId);

    int updateByPrimaryKeySelective(Room record);

    int updateByPrimaryKey(Room record);

    int updateByRoomId(String roomId);

    List selectAll();
}