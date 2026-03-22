package ru.otus.java.basic.homeworks.hw13;

public class Car implements Transport {
    private String name;
    private double fuel;
    private static final double FUEL_CONSUMPTION_PER_KM = 0.12;

    public Car(String name, double initialFuel) {
        this.name = name;
        this.fuel = initialFuel;
    }

    @Override
    public String getName() {
        return name;
    }

    public void refuel(double amount) {
        fuel += amount;
        System.out.println(name + " заправлена. Топливо: " + fuel);
    }

    public double getFuel() {
        return fuel;
    }

    @Override
    public boolean move(int distance, TerrainType terrain) {
        if (terrain == TerrainType.FOREST || terrain == TerrainType.SWAMP) {
            System.out.print("Машина не может ехать по " + terrain.getDescription() + " - ");
            return false;
        }

        double requiredFuel = distance * FUEL_CONSUMPTION_PER_KM;
        if (fuel < requiredFuel) {
            System.out.print("Недостаточно топлива (нужно " + requiredFuel + ", есть " + fuel + ") - ");
            return false;
        }

        fuel -= requiredFuel;
        System.out.print("Машина проехала " + distance + " км по " + terrain.getDescription() +
                ", остаток топлива: " + String.format("%.2f", fuel) + " - ");
        return true;
    }
}
