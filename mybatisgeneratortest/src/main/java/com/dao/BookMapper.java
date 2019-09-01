package com.dao;

import com.model.Book;
import com.model.BookExample;
import java.util.List;

public interface BookMapper {
    int insert(Book record);

    int insertSelective(Book record);

    List<Book> selectByExample(BookExample example);
}