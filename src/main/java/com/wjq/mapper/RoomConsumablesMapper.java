package com.wjq.mapper;

import com.wjq.model.RoomConsumables;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("/roomConsumablesMapper")
public interface RoomConsumablesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RoomConsumables record);

    int insertSelective(RoomConsumables record);

    RoomConsumables selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoomConsumables record);

    int updateByPrimaryKey(RoomConsumables record);

    List selectList(@Param(value = "roomName") String roomName,@Param(value = "name") String name);
}