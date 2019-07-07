package com.soecode.lyf;

import com.soecode.lyf.service.BookService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GetBeanTestBySpring {
    public static void main(String[] args) {
        //spring/spring-dao.xml", "classpath:spring/spring-service.xml
        ApplicationContext contex=new ClassPathXmlApplicationContext(new String[]{"spring/spring-dao.xml","spring/spring-service.xml"});
        System.out.println(contex.getBean(BookService.class));
        System.out.println(contex.getBean("bookServiceImpl"));

    }
}
