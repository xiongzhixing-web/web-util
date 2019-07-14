package com.soecode.lyf;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TestSynchronized {
    public static void main(String[] args) {
        /*ExecutorService executor = Executors.newFixedThreadPool(10);

        for(int i= 0;i < 10;i++){
            executor.execute(new People());
        }

        executor.shutdown();*/


    }
    static class People implements Runnable{
        private static volatile Map<String,String> data;
        private static AtomicInteger atomicInteger = new AtomicInteger();

        public Map<String,String> getData(){
            if(data == null){
                synchronized (People.class){
                    if(data == null){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        data = new HashMap<>();
                        System.out.println("data被初始化次数：" + atomicInteger.incrementAndGet());
                    }
                }
            }
            return data;
        }

        @Override
        public void run() {
            getData();
        }
    }
}
