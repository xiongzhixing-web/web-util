package com.soecode.lyf.testclassloader;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.URL;
import java.util.List;

public class ClassLoaderTest {
    public static void main(String[] args) throws DocumentException {
        URL url = ClassLoaderTest.class.getClassLoader().getResource("dom4j.xml");
        if(url == null || StringUtils.isEmpty(url.getPath())){
            throw new RuntimeException("根据文件找不到路径");
        }
        File file = new File(url.getPath());
        if(!file.exists()){
            throw new RuntimeException("文件不存在");
        }

        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        /*for (Element element: (List<Element>)root.elements()) {]
            System.out.println(element.attribute("path").getValue());
        }*/


    }
}
