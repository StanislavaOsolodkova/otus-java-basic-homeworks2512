package ru.otus.java.basic.homeworks.hw13;

public interface Transport {
    boolean move(int distance, TerrainType terrain);
    String getName();
}
