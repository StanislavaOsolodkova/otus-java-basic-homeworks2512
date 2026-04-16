package ru.otus.java.basic.homeworks.hw21;

public class ArrayFillerSingle {
    public static void main(String[] args) {
        double[] array = new double[100_000_000];

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < array.length; i++) {
            array[i] = 1.14 * Math.cos(i) * Math.sin(i * 0.2) * Math.cos(i / 1.2);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Время выполнения (один поток): " + (endTime - startTime) + " мс");
    }
}
