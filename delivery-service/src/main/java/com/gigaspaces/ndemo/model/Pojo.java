package com.gigaspaces.ndemo.model;

public class Pojo {
    private String name;
    private int age;

    public Pojo() {
    }

    public Pojo(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
