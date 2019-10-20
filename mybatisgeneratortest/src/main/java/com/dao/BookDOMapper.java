package com.dao;

import com.model.BookDO;
import com.model.BookDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BookDOMapper {
    long countByExample(BookDOExample example);

    int deleteByExample(BookDOExample example);

    int insert(BookDO record);

    int insertSelective(BookDO record);

    List<BookDO> selectByExample(BookDOExample example);

    int updateByExampleSelective(@Param("record") BookDO record, @Param("example") BookDOExample example);

    int updateByExample(@Param("record") BookDO record, @Param("example") BookDOExample example);
}