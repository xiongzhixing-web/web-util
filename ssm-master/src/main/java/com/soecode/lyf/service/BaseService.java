package com.soecode.lyf.service;

import java.util.List;

public interface BaseService {
    <T> List<String> validateParam(T t);
}
