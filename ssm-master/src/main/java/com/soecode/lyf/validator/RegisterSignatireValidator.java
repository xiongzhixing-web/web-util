package com.soecode.lyf.validator;


import com.alibaba.fastjson.JSON;
import com.soecode.lyf.vo.BaseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RegisterSignatireValidator implements Validator {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public boolean supports(Class<?> aClass) {
        return BaseVo.class.equals(aClass.getSuperclass());
    }

    @Override
    public void validate(Object o, Errors errors) {
        logger.debug("RegisterSignatireValidator#validate 开始验证签名逻辑： {}", JSON.toJSONString(o));

        return;
    }
}
