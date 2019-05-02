package com.soecode.lyf.annotation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.regex.Pattern;

/**
 * Created by jiangyunxiong on 2018/5/22.
 * <p>
 * 自定义手机格式校验注解
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {IsMobile.IsMobileValidator.class})//引进校验器
public @interface IsMobile {
    String message() default "格式错误";//校验不通过输出信息

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
        private final static Pattern PAT = Pattern.compile("^((13[0-9])|(15[^4])|(18[0-9])|(17[0-9])|(147))\\d{8}$");
        //初始化
        @Override
        public void initialize(IsMobile isMobile) {
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
            if (StringUtils.isEmpty(value)) {
                return false;
            } else {
                return PAT.matcher(value).matches();
            }
        }
    }
}
