package ru.otus.java.basic.homeworks.hw11;

class Cat extends Animal {
    public Cat(String name, float runSpeed, int endurance) {
        super(name, runSpeed, 0f, endurance);
    }

    @Override
    public float swim(int distance) {
        System.out.println(name + " не умеет плавать.");
        return -1f;
    }
}

