package ru.otus.java.basic.homeworks.hw11;

class Dog extends Animal {
    public Dog(String name, float runSpeed, float swimSpeed, int endurance) {
        super(name, runSpeed, swimSpeed, endurance);
    }

    @Override
    protected int getSwimEnduranceCostPerMeter() {
        return 2;
    }
}

