package com.soecode.lyf.web;

import com.soecode.lyf.validator.RegisterSignatireValidator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

public class BaseController{

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new RegisterSignatireValidator());
    }
}
