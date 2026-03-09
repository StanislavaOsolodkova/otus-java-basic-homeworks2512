package ru.otus.java.basic.homeworks.hw11;

abstract class Animal {
    protected String name;
    protected float runSpeed;
    protected float swimSpeed;
    protected int endurance;
    protected boolean isTired;

    public Animal(String name, float runSpeed, float swimSpeed, int endurance) {
        this.name = name;
        this.runSpeed = runSpeed;
        this.swimSpeed = swimSpeed;
        this.endurance = endurance;
        this.isTired = false;
    }

    public float run(int distance) {
        if (distance <= 0) {
            System.out.println("Ошибка, тк расстояние должно быть положительным");
        return -1f;
    }
        if (isTired) {
            System.out.println(name + " устал(а) и не может бежать!");
            return -1f;
        }

        if (endurance < distance) {
            isTired = true;
            System.out.println(name + " не хватило выносливости на дистанцию " + distance + " м");
            return -1f;
        }

        endurance -= distance;
        float time = distance / runSpeed;
        System.out.println(name + " пробежал(а) " + distance + " м за " + String.format("%.2f", time) + " сек");
        return time;
    }

    protected abstract int getSwimEnduranceCostPerMeter();
    protected boolean canSwim() {
        return swimSpeed > 0;
    }

    public float swim(int distance) {
        if (distance <= 0) {
            System.out.println("Ошибка, тк расстояние должно быть положительным.");
            return -1f;
        }

        if (!canSwim()) {
            System.out.println(name + " не умеет плавать");
            return -1f;
        }

        if (isTired) {
            System.out.println(name + " устал(а) и не может плыть!");
            return -1f;
        }

        int staminaCost = distance * getSwimEnduranceCostPerMeter();
        if (endurance < staminaCost) {
            isTired = true;
            System.out.println(name + " не хватило выносливости проплыть " + distance + " м");
            return -1f;
        }

        endurance -= staminaCost;
        float time = distance / swimSpeed;
        System.out.println(name + " проплыл(а) " + distance + " м за " + String.format("%.2f", time) + " сек");
        return time;
    }

    public void info() {
        System.out.println("Имя: " + name);
        System.out.println("Скорость бега: " + runSpeed + " м/с");
        System.out.println("Скорость плавания: " + swimSpeed + " м/с");
        System.out.println("Выносливость: " + endurance);
        System.out.println("Состояние: " + (isTired ? "УСТАЛ(А)" : "БОДР(А)"));
    }

    public String getName() {
        return name;
    }

    public boolean isTired() {
        return isTired;
    }
}

