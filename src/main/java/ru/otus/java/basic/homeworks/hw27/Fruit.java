package ru.otus.java.basic.homeworks.hw27;

public abstract class Fruit {
    protected double weight;

    public Fruit(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }
}