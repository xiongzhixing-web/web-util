package com.dao;

import com.model.People;

public interface PeopleMapper {
    int insert(People record);

    int insertSelective(People record);
}