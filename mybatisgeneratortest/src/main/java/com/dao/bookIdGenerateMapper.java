package com.dao;

import com.model.bookIdGenerate;

public interface bookIdGenerateMapper {
    int insert(bookIdGenerate record);

    int insertSelective(bookIdGenerate record);
}