package ru.otus.java.basic.homeworks.hw11;

class Dog extends Animal {
    public Dog(String name, float runSpeed, float swimSpeed, int endurance) {
        super(name, runSpeed, swimSpeed, endurance);
    }

    @Override
    public float swim(int distance) {
        if (isTired) {
            System.out.println(name + " устала и не может плыть!");
            return -1f;
        }

        int enduranceCost = distance * 2;
        if (endurance < enduranceCost) {
            isTired = true;
            System.out.println(name + " не хватило выносливости проплыть " + distance + " м");
            return -1f;
        }

        endurance -= enduranceCost;
        float time = distance / swimSpeed;
        System.out.println(name + " проплыла " + distance + " м за " + String.format("%.2f", time) + " сек");
        return time;
    }
}

