package ru.otus.java.basic.homeworks.hw11;

class Cat extends Animal {
    public Cat(String name, float runSpeed, int endurance) {
        super(name, runSpeed, 0f, endurance);
    }

    @Override
    protected int getSwimEnduranceCostPerMeter() {
        return 0; // Значение не важно, т.к. canSwim() вернет false
    }

    @Override
    protected boolean canSwim() {
        return false;
    }

    public void purr() {
        System.out.println(name + " мурлычет: Мур-мур-мур!");
    }
}

