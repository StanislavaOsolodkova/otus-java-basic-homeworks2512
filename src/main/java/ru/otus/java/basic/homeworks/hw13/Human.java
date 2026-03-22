package ru.otus.java.basic.homeworks.hw13;

public class Human {
    private String name;
    private Transport currentTransport;

    public Human(String name) {
        this.name = name;
        this.currentTransport = null;
    }

    public String getName() {
        return name;
    }

    public Transport getCurrentTransport() {
        return currentTransport;
    }

    public void setTransport(Transport transport) {
        if (currentTransport != null) {
            System.out.println(name + " покидает " + currentTransport.getName());
        }
        this.currentTransport = transport;
        if (transport != null) {
            System.out.println(name + " садится на " + transport.getName());
        }
    }

    public void leaveTransport() {
        if (currentTransport != null) {
            System.out.println(name + " покидает " + currentTransport.getName());
            currentTransport = null;
        } else {
            System.out.println(name + " не использует транспорт");
        }
    }

    public boolean move(int distance, TerrainType terrain) {
        if (currentTransport != null) {
            System.out.print(name + " пытается переместиться на " + distance + " км по " + terrain.getDescription() + " на " + currentTransport.getName() + ": ");
            boolean result = currentTransport.move(distance, terrain);
            System.out.println(result ? "Успешно" : "Не удалось");
            return result;
        } else {
            System.out.println(name + " идет пешком " + distance + " км по " + terrain.getDescription());
            return walk(distance, terrain);
        }
    }

    private boolean walk(int distance, TerrainType terrain) {
        if (terrain == TerrainType.SWAMP) {
            System.out.println("  Идти по болоту очень тяжело, но возможно");
        }
        System.out.println("  Человек прошел " + distance + " км пешком");
        return true;
    }
}
