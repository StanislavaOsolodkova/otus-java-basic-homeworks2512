package ru.otus.java.basic.homeworks.hw13;

public class AllTerrainVehicle implements Transport {
    private String name;
    private double fuel;
    private static final double FUEL_CONSUMPTION_PER_KM = 0.3;

    public AllTerrainVehicle(String name, double initialFuel) {
        this.name = name;
        this.fuel = initialFuel;
    }

    @Override
    public String getName() {
        return name;
    }

    public void refuel(double amount) {
        fuel += amount;
        System.out.println(name + " заправлен. Топливо: " + fuel);
    }

    @Override
    public boolean move(int distance, TerrainType terrain) {
        double consumptionMultiplier = 1.0;

        switch (terrain) {
            case SWAMP:
                consumptionMultiplier = 2.0; // По болоту расход больше
                break;
            case FOREST:
                consumptionMultiplier = 1.5; // По лесу тяжелее
                break;
            default:
                consumptionMultiplier = 1.0; // По равнине нормально
        }

        double requiredFuel = distance * FUEL_CONSUMPTION_PER_KM * consumptionMultiplier;

        if (fuel < requiredFuel) {
            System.out.print("Недостаточно топлива для вездехода (нужно " + requiredFuel + ", есть " + fuel + ") - ");
            return false;
        }

        fuel -= requiredFuel;
        System.out.print("Вездеход проехал " + distance + " км по " + terrain.getDescription() +
                ", расход топлива: " + requiredFuel + ", остаток: " + String.format("%.2f", fuel) + " - ");
        return true;
    }
}
