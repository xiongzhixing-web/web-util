package com.soecode.lyf.testclassloader;

public class ClassForName1 implements ComInterface{
    static {
        System.out.println("执行了静态块代码");
    }

    private static String staticField = staticMethod();

    public static String staticMethod(){
        System.out.println("执行了静态方法");
        return "给静态变量赋值";
    }

    @Override
    public void hello() {
        System.out.println(this.getClass().getName());
    }
}
