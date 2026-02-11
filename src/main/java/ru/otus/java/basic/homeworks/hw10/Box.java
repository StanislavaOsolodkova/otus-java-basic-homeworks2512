package ru.otus.java.basic.homeworks.hw10;

public class Box {
    private final int width;
    private final int height;
    private final int depth;
    private String color;
    private boolean isOpen;
    private String item;

    public Box(int width, int height, int depth, String color) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.color = color;
        this.isOpen = false;
        this.item = null;
    }

    public void open() {
        if (isOpen) {
            System.out.println("Коробка уже открыта.");
        } else {
            isOpen = true;
            System.out.println("Коробка открыта");
        }
    }

    public void close() {
        if (!isOpen) {
            System.out.println("Коробка уже закрыта.");
        } else {
            isOpen = false;
            System.out.println("Коробка закрыта");
        }
    }

    public void repaint(String newColor) {
        System.out.println("Коробка перекрашена с " + color + " на " + newColor);
        color = newColor;
    }

    public void putItem(String newItem) {
        if (!isOpen) {
            System.out.println("Коробка закрыта. Откройте коробку.");
            return;
        }

        if (item != null) {
            System.out.println("Нельзя положить предмет. В коробке уже есть: " + item);
            return;
        }

        item = newItem;
        System.out.println("Вы положили в коробку: " + item);
    }

    public void takeItem() {
        if (!isOpen) {
            System.out.println("Коробка закрыта. Откройте коробку.");
            return;
        }

        if (item == null) {
            System.out.println("В коробке пусто.");
            return;
        }

        System.out.println("Вы достали из коробки: " + item);
        item = null;
    }

    public void printInfo() {
        System.out.println("Коробка " + color + " цвета");
        System.out.println("Размер: " + width + "x" + height + "x" + depth);
        System.out.println("Состояние: " + (isOpen ? "открыта" : "закрыта"));
        System.out.println("Внутри: " + (item == null ? "ничего" : item));
    }
}