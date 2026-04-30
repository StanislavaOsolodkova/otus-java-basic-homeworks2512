package ru.otus.java.basic.homeworks.chat.server;

import ru.otus.java.basic.homeworks.chat.common.Room;
import ru.otus.java.basic.homeworks.chat.common.User;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ClientHandler implements Runnable {
    private final Server server;
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private User user;
    private String currentRoom = "general";
    private volatile long lastActivity = System.currentTimeMillis();
    private volatile boolean running = true;

    // Спам-фильтр
    private final Deque<Long> messageTimestamps = new ArrayDeque<>();
    private static final int MAX_MESSAGES_PER_WINDOW = 5;
    private static final long SPAM_WINDOW_MS = 3000;

    // Счётчик сообщений для начисления лайков
    private int messageCount = 0;

    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            socket.setSoTimeout(30_000);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания обработчика", e);
        }
    }

    @Override
    public void run() {
        try {
            sendMessage("Добро пожаловать! Используйте /register или /auth");
            while (running) {
                String line = in.readLine();
                if (line == null) break;
                lastActivity = System.currentTimeMillis();
                if (line.startsWith("/")) {
                    handleCommand(line);
                } else {
                    handleChatMessage(line);
                }
            }
        } catch (SocketTimeoutException e) {
            sendMessage("Вы отключены по таймауту");
        } catch (IOException e) {
            if (running) System.err.println("Ошибка чтения: " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    private void handleCommand(String cmdLine) {
        String[] parts = cmdLine.split(" ", 3);
        String cmd = parts[0];
        try {
            switch (cmd) {
                case "/register":
                    handleRegister(parts);
                    break;
                case "/auth":
                    handleAuth(parts);
                    break;
                case "/exit":
                    handleExit();
                    break;
                case "/w":
                    handlePrivateMessage(parts);
                    break;
                case "/changenick":
                    handleChangeNick(parts);
                    break;
                case "/activelist":
                    handleActiveList();
                    break;
                case "/shutdown":
                    handleShutdown();
                    break;
                case "/ban":
                    handleBan(parts);
                    break;
                case "/unban":
                    handleUnban(parts);
                    break;
                case "/room":
                    handleRoom(parts);
                    break;
                case "/enter":
                    handleEnter(parts);
                    break;
                case "/leave":
                    handleLeave();
                    break;
                case "/last":
                    handleLastActivity(parts);
                    break;
                case "/rate":
                    handleRate(parts);
                    break;
                default:
                    sendMessage("Неизвестная команда: " + cmd);
                    break;
            }
        } catch (Exception e) {
            sendMessage("Ошибка: " + e.getMessage());
        }
    }

    // ---------- Обработчики команд ----------
    private void requireAuth() {
        if (user == null) throw new SecurityException("Необходимо авторизоваться");
    }

    private void requireAdmin() {
        requireAuth();
        if (!"admin".equals(user.role)) throw new SecurityException("Требуются права администратора");
    }

    private void handleRegister(String[] parts) {
        if (parts.length < 3) { sendMessage("Формат: /register имя пароль"); return; }
        if (server.getAuth().register(parts[1], parts[2])) {
            sendMessage("Регистрация успешна. Теперь войдите: /auth " + parts[1] + " пароль");
        } else {
            sendMessage("Пользователь с таким именем уже существует");
        }
    }

    private void handleAuth(String[] parts) {
        if (parts.length < 3) { sendMessage("Формат: /auth имя пароль"); return; }
        String username = parts[1];
        if (server.getBanManager().isBanned(username)) {
            sendMessage("Вы забанены");
            return;
        }
        User u = server.getAuth().authenticate(username, parts[2]);
        if (u != null) {
            this.user = u;
            server.registerClient(u.username, this);
            server.getDb().updateLastOnline(u.username);
            sendMessage("Добро пожаловать, " + u.username + "! Роль: " + u.role);
            // Отправить историю комнаты
            List<String> history = server.getDb().getRoomHistory(currentRoom, 20);
            for (String msg : history) sendMessage(msg);
            server.broadcastMessage(u.username + " вошёл в чат");
            server.getDb().updateRoomActivity(currentRoom);
        } else {
            sendMessage("Неверный логин или пароль");
        }
    }

    private void handleExit() {
        sendMessage("/exitok");
        running = false;
    }

    private void handlePrivateMessage(String[] parts) {
        requireAuth();
        if (parts.length < 3) { sendMessage("Формат: /w имя сообщение"); return; }
        String to = parts[1];
        String msg = parts[2];
        server.sendPrivate(user.username, to, msg);
        server.getDb().saveMessage("private", user.username, msg, true, to);
    }

    private void handleChangeNick(String[] parts) {
        requireAuth();
        if (parts.length < 2) { sendMessage("Формат: /changenick новыйник"); return; }
        String newNick = parts[1];
        if (server.getDb().changeNick(user.username, newNick)) {
            server.unregisterClient(user.username);
            user = new User(user.id, newNick, user.role, user.rating, user.likeBalance);
            server.registerClient(newNick, this);
            sendMessage("Ник изменён на " + newNick);
            server.broadcastMessage("Пользователь " + user.username + " сменил ник на " + newNick);
        } else {
            sendMessage("Ник занят или ошибка");
        }
    }

    private void handleActiveList() {
        requireAuth();
        List<String> users = server.getActiveUsernames();
        sendMessage("Активные пользователи: " + String.join(", ", users));
    }

    private void handleShutdown() {
        requireAdmin();
        sendMessage("Сервер останавливается...");
        server.shutdown();
    }

    private void handleBan(String[] parts) {
        requireAdmin();
        if (parts.length < 3) { sendMessage("Формат: /ban имя длительность (например, 30m, perm)"); return; }
        server.getBanManager().ban(parts[1], parts[2], user.username);
        ClientHandler target = server.getClient(parts[1]);
        if (target != null) {
            target.sendMessage("Вы забанены! Переподключение невозможно.");
            target.disconnect();
        }
        sendMessage("Пользователь " + parts[1] + " забанен.");
    }

    private void handleUnban(String[] parts) {
        requireAdmin();
        if (parts.length < 2) { sendMessage("Формат: /unban имя"); return; }
        server.getBanManager().unban(parts[1]);
        sendMessage("Пользователь " + parts[1] + " разбанен.");
    }

    private void handleRoom(String[] parts) {
        requireAuth();
        if (parts.length < 2) {
            sendMessage("Подкоманды: /room list, /room create [имя] [пароль?], /room delete имя");
            return;
        }
        switch (parts[1]) {
            case "list": {
                List<String> rooms = server.getDb().getRoomList();
                sendMessage("Комнаты: " + String.join(", ", rooms));
                break;
            }
            case "create": {
                if (parts.length < 3) { sendMessage("Формат: /room create имя [пароль]"); break; }
                String roomName = parts[2];
                String password = (parts.length > 3) ? parts[3] : null;
                if (server.getDb().countUserRooms(user.id) >= 5) {
                    sendMessage("Вы не можете создавать более 5 комнат");
                    break;
                }
                String passHash = (password != null) ? AuthService.sha256(password) : null;
                if (server.getDb().createRoom(roomName, user.id, passHash)) {
                    sendMessage("Комната '" + roomName + "' создана");
                } else {
                    sendMessage("Ошибка: комната уже существует");
                }
                break;
            }
            case "delete": {
                if (parts.length < 3) { sendMessage("Формат: /room delete имя"); break; }
                Room room = server.getDb().getRoom(parts[2]);
                if (room == null) { sendMessage("Комната не найдена"); break; }
                if (room.ownerId != user.id && !"admin".equals(user.role)) {
                    sendMessage("Только владелец или админ может удалить комнату");
                    break;
                }
                server.getDb().deleteRoom(parts[2]);
                sendMessage("Комната удалена");
                break;
            }
            default:
                sendMessage("Неизвестная подкоманда room");
                break;
        }
    }

    private void handleEnter(String[] parts) {
        requireAuth();
        if (parts.length < 2) { sendMessage("Формат: /enter комната [пароль]"); return; }
        String roomName = parts[1];
        Room room = server.getDb().getRoom(roomName);
        if (room == null) { sendMessage("Комната не найдена"); return; }
        if (room.hasPassword()) {
            String givenPass = (parts.length > 2) ? parts[2] : "";
            if (!AuthService.sha256(givenPass).equals(room.passwordHash)) {
                sendMessage("Неверный пароль комнаты");
                return;
            }
        }
        currentRoom = roomName;
        server.getDb().updateRoomActivity(roomName);
        sendMessage("Вы вошли в комнату " + roomName);
        List<String> history = server.getDb().getRoomHistory(roomName, 20);
        history.forEach(this::sendMessage);
    }

    private void handleLeave() {
        requireAuth();
        if (currentRoom.equals("general")) {
            sendMessage("Вы уже в общей комнате");
        } else {
            currentRoom = "general";
            sendMessage("Вы вернулись в общую комнату");
            List<String> history = server.getDb().getRoomHistory("general", 20);
            history.forEach(this::sendMessage);
        }
    }

    private void handleLastActivity(String[] parts) {
        requireAuth();
        if (parts.length < 2) { sendMessage("Формат: /last имя"); return; }
        Long lastOnline = server.getDb().getLastOnline(parts[1]);
        if (lastOnline == null) { sendMessage("Пользователь не найден"); }
        else sendMessage("Последний раз в сети: " + new java.util.Date(lastOnline * 1000));
    }

    private void handleRate(String[] parts) {
        requireAuth();
        if (parts.length < 3) { sendMessage("Формат: /rate имя +/-число"); return; }
        String target = parts[1];
        String deltaStr = parts[2];
        if (!deltaStr.startsWith("+") && !deltaStr.startsWith("-")) {
            sendMessage("Нужно указать +/- число, например +1 или -1");
            return;
        }
        int delta = Integer.parseInt(deltaStr);
        if (delta == 0) return;
        if (target.equals(user.username)) { sendMessage("Нельзя менять рейтинг самому себе"); return; }
        int balance = server.getDb().getLikeBalance(user.username);
        if (Math.abs(delta) > balance) {
            sendMessage("Недостаточно лайков (ваш баланс: " + balance + ")");
            return;
        }
        server.getDb().updateLikeBalance(user.username, -Math.abs(delta));
        server.getDb().updateUserRating(target, delta);
        sendMessage("Рейтинг " + target + " изменён. Ваш оставшийся баланс лайков: " +
                (balance - Math.abs(delta)));
    }

    // ---------- Обработка чат-сообщений ----------
    private void handleChatMessage(String msg) {
        if (user == null) {
            sendMessage("Сначала авторизуйтесь");
            return;
        }
        // Спам-фильтр
        long now = System.currentTimeMillis();
        messageTimestamps.addLast(now);
        while (!messageTimestamps.isEmpty() && now - messageTimestamps.getFirst() > SPAM_WINDOW_MS) {
            messageTimestamps.removeFirst();
        }
        if (messageTimestamps.size() > MAX_MESSAGES_PER_WINDOW) {
            sendMessage("Слишком много сообщений! Подождите.");
            return;
        }
        // Фильтр плохих слов
        msg = server.getProfanityFilter().filter(msg);
        // Время
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String formatted = String.format("[%s] %s: %s", time, user.username, msg);
        server.broadcastToRoom(currentRoom, formatted);
        server.getDb().saveMessage(currentRoom, user.username, msg, false, null);

        // Начисление лайков за активность (каждые 10 сообщений +1 лайк)
        messageCount++;
        if (messageCount % 10 == 0) {
            server.getDb().updateLikeBalance(user.username, 1);
            sendMessage("Вы получили +1 лайк за активность. Баланс: " + server.getDb().getLikeBalance(user.username));
        }
    }

    // ---------- Вспомогательные методы ----------
    public void sendMessage(String msg) {
        out.println(msg);
        if (out.checkError()) {
            disconnect();
        }
    }

    public boolean isAuthenticated() {
        return user != null;
    }

    public String getCurrentRoom() {
        return currentRoom;
    }

    public long getLastActivity() {
        return lastActivity;
    }

    public void disconnect() {
        running = false;
        if (user != null) {
            server.unregisterClient(user.username);
            server.broadcastMessage(user.username + " покинул чат");
        }
        try { socket.close(); } catch (IOException ignored) {}
    }
}