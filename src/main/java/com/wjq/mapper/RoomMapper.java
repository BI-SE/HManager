package com.wjq.mapper;

import com.wjq.model.Room;
import org.apache.ibatis.annotations.Param;
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

    int updateActiveByRoomId(String roomId);

    int updateStateByRoomId(@Param(value = "roomId") String roomId,@Param(value = "status") String status);

    List selectAll(@Param(value = "status") String status,@Param(value = "roomType")
            String roomType,@Param(value = "roomName") String roomName);
}