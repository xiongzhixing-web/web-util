package com.soecode.lyf.annotation;

import javax.validation.*;
import java.lang.annotation.*;
import java.util.List;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {IsSpecificEnum.IsExistEnum.class}
)//引进校验器
public @interface IsSpecificEnum {
    String message() default "传入值不在指定范围";//校验不通过输出信息
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] enumVal() default {};

    class IsExistEnum implements ConstraintValidator<IsSpecificEnum, Object> {
        private String[] enumStrs;
        public void initialize(IsSpecificEnum isSpecificEnum) {
            enumStrs = isSpecificEnum.enumVal();
        }

        public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
            String val = String.valueOf(o);
            if(enumStrs != null && enumStrs.length > 0){
                for(String str:enumStrs){
                    if(str.equals(val)){
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
