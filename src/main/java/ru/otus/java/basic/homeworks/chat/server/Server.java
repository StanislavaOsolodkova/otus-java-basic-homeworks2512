package ru.otus.java.basic.homeworks.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Server {
    private final int port;
    private final ExecutorService threadPool;
    private final Map<String, ClientHandler> activeClients = new ConcurrentHashMap<>(); // username -> handler
    private final DatabaseService db;
    private final AuthService auth;
    private final BanManager banManager;
    private final ProfanityFilter profanityFilter;
    private final ScheduledExecutorService scheduler;
    private volatile boolean running = true;

    public Server(int port) {
        this.port = port;
        this.threadPool = Executors.newCachedThreadPool();
        this.db = new DatabaseService();
        this.auth = new AuthService(db);
        this.banManager = new BanManager(db);
        this.profanityFilter = new ProfanityFilter();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        // комната по умолчанию всегда есть
        if (db.getRoom("general") == null) {
            db.createRoom("general", 0, null); // системная комната
        }
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::kickInactive, 1, 1, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(this::deleteOldRooms, 1, 1, TimeUnit.DAYS);
        scheduler.scheduleAtFixedRate(this::increaseLikeBalances, 10, 10, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(banManager::expireBans, 1, 1, TimeUnit.MINUTES);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port);
            while (running) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(this, socket);
                threadPool.execute(handler);
            }
        } catch (IOException e) {
            if (running) {
                System.err.println("Ошибка сервера: " + e.getMessage());
            }
        } finally {
            shutdown();
        }
    }

    public void registerClient(String username, ClientHandler handler) {
        activeClients.put(username, handler);
    }

    public void unregisterClient(String username) {
        activeClients.remove(username);
    }

    public ClientHandler getClient(String username) {
        return activeClients.get(username);
    }

    public List<String> getActiveUsernames() {
        return List.copyOf(activeClients.keySet());
    }

    public void broadcastToRoom(String room, String message) {
        activeClients.values().stream()
                .filter(c -> room.equals(c.getCurrentRoom()))
                .forEach(c -> c.sendMessage(message));
    }

    public void broadcastMessage(String message) {
        activeClients.values().forEach(c -> c.sendMessage(message));
    }

    public void sendPrivate(String from, String to, String message) {
        ClientHandler target = activeClients.get(to);
        if (target != null) {
            target.sendMessage(String.format("[Личное от %s]: %s", from, message));
            ClientHandler sender = activeClients.get(from);
            if (sender != null) sender.sendMessage(String.format("[Личное для %s]: %s", to, message));
        } else {
            ClientHandler sender = activeClients.get(from);
            if (sender != null) sender.sendMessage("Пользователь " + to + " не в сети");
        }
    }

    public AuthService getAuth() { return auth; }
    public DatabaseService getDb() { return db; }
    public BanManager getBanManager() { return banManager; }
    public ProfanityFilter getProfanityFilter() { return profanityFilter; }

    private void kickInactive() {
        long now = System.currentTimeMillis();
        activeClients.values().removeIf(handler -> {
            if (now - handler.getLastActivity() > 20 * 60 * 1000 && handler.isAuthenticated()) {
                handler.sendMessage("Вы отключены за неактивность");
                handler.disconnect();
                return true;
            }
            return false;
        });
    }

    private void deleteOldRooms() {
        long sevenDaysAgo = System.currentTimeMillis() / 1000 - 7 * 24 * 3600;
        db.deleteOldRooms(sevenDaysAgo);
    }

    private void increaseLikeBalances() {
        // Даём +1 лайк каждому активному пользователю каждые 10 минут
        activeClients.keySet().forEach(username -> db.updateLikeBalance(username, 1));
    }

    public void shutdown() {
        running = false;
        activeClients.values().forEach(ClientHandler::disconnect);
        threadPool.shutdown();
        scheduler.shutdown();
    }
}
