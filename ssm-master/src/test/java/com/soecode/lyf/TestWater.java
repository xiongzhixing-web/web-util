package com.soecode.lyf;

import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestWater {
    private static int[] height =  {1,8,6,2,5,4,8,3,7};
    public static void main(String[] args) {
        System.out.println(new TestWater().maxArea(height));
    }
    public int maxArea(int[] height) {
        Map<String,Integer> data = new LinkedHashMap<>();

        for(int i = 0;i < height.length - 1;i++){
            for(int j = i + 1;j < height.length;j++){
                data.put(i + "-" + j,(j - i) * Math.min(height[i],height[j]));
            }
        }

        return data.values().parallelStream().max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        }).get();
    }

}
