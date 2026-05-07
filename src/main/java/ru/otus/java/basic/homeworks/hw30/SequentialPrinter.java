package ru.otus.java.basic.homeworks.hw30;

public class SequentialPrinter {
    private int turn = 0;  // 0 - A, 1 - B, 2 - C
    private int count = 0; // сколько всего символов напечатано (для завершения)

    public synchronized void print(char letter, int expectedTurn, int maxPrints) {
        while (turn != expectedTurn && count < maxPrints) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        if (count >= maxPrints) {
            notifyAll();
            return;
        }
        // печатаем букву
        System.out.print(letter);
        count++;
        // передаём ход следующему
        turn = (turn + 1) % 3;
        notifyAll();
    }

    public boolean isFinished(int maxPrints) {
        return count >= maxPrints;
    }
}
