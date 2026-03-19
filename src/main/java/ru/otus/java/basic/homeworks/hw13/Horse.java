package ru.otus.java.basic.homeworks.hw13;

public class Horse implements Transport {
    private String name;
    private double stamina;
    private final double staminaConsumptionPerKm = 0.5;

    public Horse(String name, double initialStamina) {
        this.name = name;
        this.stamina = initialStamina;
    }

    @Override
    public String getName() {
        return name;
    }

    public void rest() {
        stamina += 10;
        System.out.println(name + " отдохнула. Сил стало: " + stamina);
    }

    @Override
    public boolean move(int distance, TerrainType terrain) {
        if (terrain == TerrainType.SWAMP) {
            System.out.print("Лошадь не может идти по болоту - ");
            return false;
        }

        double requiredStamina = distance * staminaConsumptionPerKm;
        if (terrain == TerrainType.FOREST) {
            requiredStamina *= 1.5; // В лесу тяжелее
        }

        if (stamina < requiredStamina) {
            System.out.print("У лошади недостаточно сил (нужно " + requiredStamina + ", есть " + stamina + ") - ");
            return false;
        }

        stamina -= requiredStamina;
        System.out.print("Лошадь прошла " + distance + " км по " + terrain.getDescription() +
                ", остаток сил: " + String.format("%.2f", stamina) + " - ");
        return true;
    }
}
