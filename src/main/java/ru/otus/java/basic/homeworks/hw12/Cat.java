package ru.otus.java.basic.homeworks.hw12;

class Cat {
    private String name;
    private int appetite;
    private boolean isFull;

    public Cat(String name, int appetite) {
        this.name = name;
        this.appetite = appetite;
        this.isFull = false;
    }

    public String getName() {
        return name;
    }

    public int getAppetite() {
        return appetite;
    }

    public boolean isFull() {
        return isFull;
    }

    @Override
    public String toString() {
        return "Кот " + name +
                " (аппетит: " + appetite +
                ", сытость: " + (isFull ? "сыт" : "голоден") + ")";
    }

    public void eat(Plate plate) {
        if (isFull) {
            System.out.println(name + " уже сыт(а) и не хочет есть");
            return;
        }

        boolean f = plate.decreaseFood(appetite);

        if (f) {
            isFull = true;
            System.out.println(name + " покушал(а) и теперь сыт(а)");
        } else {
            System.out.println(name + " не смог(ла) поесть: в тарелке недостаточно еды");
        }
    }
}