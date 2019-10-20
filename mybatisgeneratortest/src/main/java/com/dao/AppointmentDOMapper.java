package com.dao;

import com.model.AppointmentDO;
import com.model.AppointmentDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppointmentDOMapper {
    long countByExample(AppointmentDOExample example);

    int deleteByExample(AppointmentDOExample example);

    int insert(AppointmentDO record);

    int insertSelective(AppointmentDO record);

    List<AppointmentDO> selectByExample(AppointmentDOExample example);

    int updateByExampleSelective(@Param("record") AppointmentDO record, @Param("example") AppointmentDOExample example);

    int updateByExample(@Param("record") AppointmentDO record, @Param("example") AppointmentDOExample example);
}