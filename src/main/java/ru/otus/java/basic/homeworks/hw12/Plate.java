package ru.otus.java.basic.homeworks.hw12;

class Plate {
    private int maxFood;
    private int currentFood;

    public Plate(int maxFood) {
        this.maxFood = maxFood;
        this.currentFood = maxFood;
    }

    public int getCurrentFood() {
        return currentFood;
    }

    public int getMaxFood() {
        return maxFood;
    }

    @Override
    public String toString() {
        return "Тарелка: " + currentFood + "/" + maxFood + " еды";
    }

    public void addFood(int amount) {
        if (amount > 0) {
            int newFood = currentFood + amount;
            currentFood = Math.min(newFood, maxFood);
            System.out.println("Добавлено " + amount + " еды. Теперь в тарелке: " + currentFood);
        }
    }

    public boolean decreaseFood(int amount) {
        if (amount <= 0) {
            return false;
        }

        if (currentFood >= amount) {
            currentFood -= amount;
            return true;
        } else {
            return false;
        }
    }
}
