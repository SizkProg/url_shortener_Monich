package url_shortener_Monich;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class User {
    private static int nextid = 0;
    private static Map<String, User> users = new HashMap<>();


    private final String login;
    private final int id;

    private User(int id, String login) {
        this.id = id;
        this.login = login;
    }

    public static User createOrGetUser(String login) {
        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Login cannot be null or empty");
        }

        String lowerLogin = login.trim().toLowerCase();

        if (users.containsKey(lowerLogin)) {
            System.out.println("Авторизация существующего пользователя: " + login);
            return users.get(lowerLogin);
        } else {
            User newUser = new User(++nextid, lowerLogin);
            users.put(lowerLogin, newUser);
            System.out.println("Зарегистрирован новый пользователь: " + login + "\n ID: " + newUser.id + ")");
            return newUser;
        }

    }

    public static int getNextid() {
        return nextid;
    }

    public static void setNextid(int nextid) {
        User.nextid = nextid;
    }

    public static Map<String, User> getUsers() {
        return users;
    }

    public static void setUsers(Map<String, User> users) {
        User.users = users;
    }

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", login='" + login + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, id);
    }
}
