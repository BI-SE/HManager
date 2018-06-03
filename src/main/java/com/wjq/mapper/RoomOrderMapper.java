package com.wjq.mapper;

import com.wjq.model.RoomOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("roomOrderMapper")
public interface RoomOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RoomOrder record);

    int insertSelective(RoomOrder record);

    RoomOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoomOrder record);

    int updateStatusByRoomId(String roomId);

    int updateStatusByOrderNo(@Param(value = "orderNo") String orderNo,@Param(value = "status") String status);

    int updateByPrimaryKey(RoomOrder record);

    List selectAll(@Param(value = "userName") String userName,@Param(value = "roomName") String roomName);


    RoomOrder selectBRoomId(String roomId);

    List selectOrderChat();

}