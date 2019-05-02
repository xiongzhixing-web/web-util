package com.soecode.lyf.service.impl;

import com.soecode.lyf.service.BaseService;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

public class BaseServiceImpl implements BaseService {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    public <T> List<String> validateParam(T t) {
        List<String> paramsMesList = new ArrayList<String>();

        Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(t);
        Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<T> item = iterator.next();
            paramsMesList.add(item.getPropertyPath() + item.getMessage());
        }
        Collections.sort(paramsMesList);
        return paramsMesList;
    }
}
