package ru.otus.java.basic.homeworks.hw21;

public class ArrayFillerMulti {
    public static void main(String[] args) throws InterruptedException {
        double[] array = new double[100_000_000];

        long startTime = System.currentTimeMillis();

        Thread[] threads = new Thread[4];
        int chunkSize = array.length / 4;

        for (int t = 0; t < 4; t++) {
            final int start = t * chunkSize;
            final int end = (t == 3) ? array.length : (t + 1) * chunkSize;

            threads[t] = new Thread(() -> {
                for (int i = start; i < end; i++) {
                    array[i] = 1.14 * Math.cos(i) * Math.sin(i * 0.2) * Math.cos(i / 1.2);
                }
            });
            threads[t].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Время выполнения (4 потока): " + (endTime - startTime) + " мс");
    }
}
