package com.soecode.lyf.util;


import com.soecode.lyf.vo.BookVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.util.DigestUtils;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SignatureUtil {
    private static final Logger logger = LoggerFactory.getLogger(SignatureUtil.class);

    public static List<String> getRequestParam(Object o, Set<String> excludeProperties) {
        List<String> properties = new ArrayList<>();
        if (o == null) {
            return properties;
        }
        PropertyDescriptor[] propertyDescriptors = ReflectUtils.getBeanProperties(o.getClass());
        try {

            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String propertyName = propertyDescriptor.getName();
                if(excludeProperties != null && excludeProperties.contains(propertyName)){
                    continue;
                }
                String propertyVal = String.valueOf(propertyDescriptor.getReadMethod().invoke(o, new Object[0]));
                properties.add(propertyName + "=" + propertyVal);
            }
            return properties;
        } catch (IllegalAccessException e) {
            logger.error("catch a exception.",e);
            return properties;
        } catch (InvocationTargetException e) {
            logger.error("catch a exception.",e);
            return properties;
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        BookVo bookVo = new BookVo();
        bookVo.setBookId(1000);
        bookVo.setBookName("java");
        bookVo.setAppKey("appKey");
        bookVo.setTimestamp(new Date().getTime());

        List<String> paramList = SignatureUtil.getRequestParam(bookVo,new HashSet<>(Arrays.asList("bookId")));
        StringBuilder stringBuilder = new StringBuilder();
        for(String param:paramList){
            stringBuilder.append(param + "&");
        }
        System.out.println(DigestUtils.md5Digest(stringBuilder.toString().getBytes("utf-8")));

    }

}
