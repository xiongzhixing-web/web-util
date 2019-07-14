package com.soecode.lyf.util;


import com.alibaba.fastjson.JSON;
import com.soecode.lyf.vo.BookVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.util.DigestUtils;

import java.beans.PropertyDescriptor;
import java.util.*;

public class SignatureUtil {
    private static final Logger logger = LoggerFactory.getLogger(SignatureUtil.class);

    public static List<String> getRequestParamList(Object o, Set<String> excludeProperties) throws Exception {
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
            Collections.sort(properties);
            return properties;
        } catch (Exception e) {
            logger.error("SignatureUtil#getRequestParam catch a exception.",e);
            throw e;
        }
    }

    public static String getRequestParamStr(Object o, Set<String> excludeProperties) throws Exception {
        List<String> keyValList = getRequestParamList(o,excludeProperties);
        StringBuilder stringBuilder = new StringBuilder("");
        keyValList.stream().forEach(str -> stringBuilder.append(str).append("&"));
        return stringBuilder.toString().substring(0,stringBuilder.length() - 1);
    }

    public static void main(String[] args) throws Exception {
        BookVo bookVo = new BookVo();
        bookVo.setBookId(1000);
        bookVo.setBookName("java");
        bookVo.setAppKey("appKey");
        bookVo.setTimestamp(new Date().getTime());
        String str = SignatureUtil.getRequestParamStr(bookVo,new HashSet<String>(Arrays.asList("sign")));
        logger.info("sign str={}",str);
        String sign = DigestUtils.md5DigestAsHex(str.getBytes());
        bookVo.setSign(sign);

        System.out.println(JSON.toJSONString(bookVo));
    }

}
