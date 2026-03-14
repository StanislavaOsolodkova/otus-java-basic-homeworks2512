package ru.otus.java.basic.homeworks.hw12;

public class Main {
    public static void main(String[] args) {

        Plate plate = new Plate(30);

        Cat[] cats = {
                new Cat("Борис", 10),
                new Cat("Мурка", 15),
                new Cat("Рыжик", 8),
                new Cat("Луна", 12),
                new Cat("Игорь", 5)
        };

        System.out.println("Коты до кормежки:");
        for (Cat cat : cats) {
            System.out.println(cat);
        }
        System.out.println();

        System.out.println("Процесс кормежки:");
        for (Cat cat : cats) {
            cat.eat(plate);
        }
        System.out.println();

        System.out.println("Коты после кормежки:");
        for (Cat cat : cats) {
            System.out.println(cat);
        }
        System.out.println();

        System.out.println("Состояние тарелки после кормежки: " + plate);

        System.out.println("\n--- Добавляем еду в тарелку ---");
        plate.addFood(20);
        System.out.println("После добавления: " + plate);

        System.out.println("\n--- Пытаемся добавить слишком много еды ---");
        plate.addFood(100);
        System.out.println("Результат: " + plate);
    }
}
