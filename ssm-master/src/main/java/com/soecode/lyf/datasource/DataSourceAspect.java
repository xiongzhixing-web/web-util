package com.soecode.lyf.datasource;


import com.alibaba.fastjson.JSON;
import com.soecode.lyf.annotation.DataSourceChange;
import com.soecode.lyf.exception.DataSourceAspectException;
import javafx.scene.chart.PieChart;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 定义数据源的AOP切面，通过该Service的方法名判断是应该走读库还是写库
 *
 * @author wanghongwei
 * <p>
 * 2018年10月10日-下午2:47:04
 */
@Aspect
@Component
public class DataSourceAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceAspect.class);

    @Pointcut("execution(* com.soecode.lyf.dao.*.*(..))")
    private void pointCut(){}


    @Around("pointCut()")
    //@Around(value = "@annotation(dataSourceChange)")
    //@Around(value = "@annotation(dataSourceChange)")
    public Object doAround(ProceedingJoinPoint pjp) throws DataSourceAspectException, NoSuchMethodException {
        Method method = ((MethodSignature)pjp.getSignature()).getMethod();
        DataSourceChange dataSourceChange = method.getAnnotation(DataSourceChange.class);
        Object retVal = null;
        boolean selectedDataSource = false;
        try {
            if (null != dataSourceChange) {
                selectedDataSource = true;
                if (dataSourceChange.slave()) {
                    DynamicDataSource.useSlave();
                } else {
                    DynamicDataSource.useMaster();
                }
            }
            retVal = pjp.proceed();
        } catch (Throwable e) {
            LOGGER.error("处理数据异常 参数={}", JSON.toJSONString(pjp.getArgs()), e);
            throw new DataSourceAspectException("处理数据异常", e);
        } finally {
            if (selectedDataSource) {
                DynamicDataSource.reset();
            }
        }
        return retVal;
    }

}
