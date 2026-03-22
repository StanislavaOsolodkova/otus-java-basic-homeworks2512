package ru.otus.java.basic.homeworks.hw13;

public enum TerrainType {
    FOREST("густой лес"),
    PLAIN("равнина"),
    SWAMP("болото");

    private final String description;

    TerrainType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
