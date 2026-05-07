package ru.otus.java.basic.homeworks.hw27;

import java.util.ArrayList;
import java.util.List;

public class Box<T extends Fruit> {
    private List<T> fruits = new ArrayList<>();

    public void add(T fruit) {
        fruits.add(fruit);
    }

    public void addAll(List<? extends T> newFruits) {
        fruits.addAll(newFruits);
    }

    public double weight() {
        double total = 0;
        for (T fruit : fruits) {
            total += fruit.getWeight();
        }
        return total;
    }

    public boolean compare(Box<? extends Fruit> other) {
        return Math.abs(this.weight() - other.weight()) < 0.0001;
    }

    public void transfer(Box<? super T> other) {
        for (T fruit : fruits) {
            other.add(fruit);
        }
        fruits.clear();
    }

    public int size() {
        return fruits.size();
    }
}
