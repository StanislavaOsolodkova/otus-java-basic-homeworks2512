package ru.otus.java.basic.homeworks.hw13;

public class Main {
    public static void main(String[] args) {
        Human human = new Human("Иван");
        Car car = new Car("Toyota", 10.0);
        Horse horse = new Horse("Буцефал", 20.0);
        Bicycle bicycle = new Bicycle("Stels");
        AllTerrainVehicle atv = new AllTerrainVehicle("Урал", 30.0);

        System.out.println("1. Движение пешком:");
        human.move(5, TerrainType.PLAIN);
        human.move(2, TerrainType.SWAMP);
        System.out.println();

        System.out.println("2. Поездки на машине:");
        human.setTransport(car);
        human.move(30, TerrainType.PLAIN); // Успешно
        human.move(10, TerrainType.FOREST); // Не может
        human.move(5, TerrainType.SWAMP); // Не может
        human.leaveTransport();
        System.out.println();

        System.out.println("3. Поездки на лошади:");
        human.setTransport(horse);
        human.move(15, TerrainType.PLAIN); // Успешно
        human.move(10, TerrainType.FOREST); // Успешно с доп. затратами
        human.move(5, TerrainType.SWAMP); // Не может
        horse.rest(); // Отдыхаем
        human.move(5, TerrainType.PLAIN); // Снова можем ехать
        human.leaveTransport();
        System.out.println();

        System.out.println("4. Поездки на велосипеде:");
        human.setTransport(bicycle);
        human.move(10, TerrainType.PLAIN);
        human.move(5, TerrainType.FOREST);
        human.move(2, TerrainType.SWAMP); // Не может
        human.leaveTransport();
        System.out.println();

        System.out.println("5. Поездки на вездеходе:");
        human.setTransport(atv);
        human.move(20, TerrainType.PLAIN);
        human.move(10, TerrainType.FOREST);
        human.move(5, TerrainType.SWAMP); // Может, но с большим расходом
        human.move(30, TerrainType.SWAMP); // Не хватит топлива
        atv.refuel(20); // Заправляемся
        human.move(30, TerrainType.SWAMP); // Теперь хватит
        human.leaveTransport();
        System.out.println();

        System.out.println("6. Попытка пересесть:");
        human.setTransport(car);
        human.setTransport(bicycle); // Автоматически покинет машину
        System.out.println();
    }
}
