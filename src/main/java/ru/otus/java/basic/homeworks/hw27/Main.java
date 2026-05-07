package ru.otus.java.basic.homeworks.hw27;

public class Main {
    public static void main(String[] args) {
        Box<Apple> appleBox = new Box<>();
        appleBox.add(new Apple());
        appleBox.add(new Apple());

        Box<Orange> orangeBox = new Box<>();
        orangeBox.add(new Orange());
        orangeBox.add(new Orange());
        orangeBox.add(new Orange());

        Box<Fruit> mixedBox = new Box<>();
        mixedBox.add(new Apple());
        mixedBox.add(new Orange());

        System.out.println("Apple box weight: " + appleBox.weight());
        System.out.println("Orange box weight: " + orangeBox.weight());
        System.out.println("Mixed box weight: " + mixedBox.weight());
        System.out.println("Apple == Orange? " + appleBox.compare(orangeBox));

        appleBox.transfer(mixedBox);
        System.out.println("After transfer: appleBox size = " + appleBox.size());
        System.out.println("Mixed box size = " + mixedBox.size());
    }
}