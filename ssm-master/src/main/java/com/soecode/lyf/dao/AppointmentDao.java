package com.soecode.lyf.dao;

import com.soecode.lyf.annotation.DataSourceChange;
import org.apache.ibatis.annotations.Param;

import com.soecode.lyf.entity.Appointment;

public interface AppointmentDao {

	/**
	 * 插入预约图书记录
	 * 
	 * @param bookId
	 * @param studentId
	 * @return 插入的行数
	 */
	int insertAppointment(@Param("bookId") long bookId, @Param("studentId") long studentId);

	/**
	 * 通过主键查询预约图书记录，并且携带图书实体
	 * 
	 * @param bookId
	 * @param studentId
	 * @return
	 */
	//@DataSourceChange(slave = true)
	Appointment queryByKeyWithBook(@Param("bookId") long bookId, @Param("studentId") long studentId);

}
