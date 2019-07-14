package com.soecode.lyf;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestLamda {
    public static void main(String[] args) {
        List<People> peopleList = new ArrayList<>();
        peopleList.add(new People(21,"zs"));
        peopleList.add(new People(12,"ls"));
        peopleList.add(new People(23,"ww"));
        peopleList.add(new People(23,"ww"));

        System.out.println(peopleList.parallelStream().map(p -> p.getAge()).reduce((sum,age) -> sum += age).get());

        System.out.println(JSON.toJSONString(peopleList.parallelStream().distinct().collect(Collectors.toList())));



    }

    static class People{
        private Integer age;
        private String name;

        public People(Integer age, String name) {
            this.age = age;
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            People people = (People) o;

            if (!age.equals(people.age)) return false;
            return name.equals(people.name);
        }

        @Override
        public int hashCode() {
            int result = age.hashCode();
            result = 31 * result + name.hashCode();
            return result;
        }
    }
}
