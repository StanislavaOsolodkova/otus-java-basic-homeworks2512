package ru.otus.java.basic.homeworks.hw13;

public class Bicycle implements Transport {
    private String name;

    public Bicycle(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean move(int distance, TerrainType terrain) {
        if (terrain == TerrainType.SWAMP) {
            System.out.print("Велосипед не может ехать по болоту - ");
            return false;
        }

        String condition = "хорошо";
        if (terrain == TerrainType.FOREST) {
            condition = "с трудом (по лесу тяжело)";
        }

        System.out.print("Велосипед проехал " + distance + " км по " + terrain.getDescription() + " " + condition + " - ");
        return true;
    }
}
