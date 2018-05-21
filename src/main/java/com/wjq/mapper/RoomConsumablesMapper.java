package com.wjq.mapper;

import com.wjq.model.RoomConsumables;
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

    List selectList(RoomConsumables roomConsumables);
}