package url_shortener_Monich;


import url_shortener_Monich.exceptions.InvalidUrlException;
import url_shortener_Monich.exceptions.UrlNotFoundException;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    private static final ShortURLService service = new ShortURLService();
    private static final Scanner scanner = new Scanner(System.in);
    private static User user = getUserObject();

    public static void main(String[] args) {

        boolean running = true;

        while (running) {
            printMenu();
            int choice = getIntInput("Выберите действие: ");

            try {
                switch (choice) {
                    case 1 -> createShortUrl();
                    case 2 -> getOriginalUrl();
                    case 3 -> service.showAllUrls(user.getLogin());
                    case 4 -> changeUserObject();
                    case 5 -> System.out.println("6");
                    case 0 -> {
                        System.out.println("Выход из программы");
                        running = false;
                    }
                    default -> System.out.println("Неверный выбор");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }

        scanner.close();

    }

    private static void printMenu() {

        String[] menuItems = {
                "1. 📝 Создать короткую ссылку",
                "2. 🔍 Перейти по URL",
                "3. 📋 Показать мои ссылки",
                "4. 👤 Сменить пользователя",
                "5. ❓ Помощь",
                "0. 🚪 Выход"
        };

        for (String menuItem : menuItems) {
            System.out.println(menuItem);
        }

    }


    private static void createShortUrl() throws InvalidUrlException {
        System.out.print("Введите полный URL: ");
        String originalUrl = scanner.nextLine();
        int accessCountPossible = getIntInput("Введите число возможных переходов");

        if (accessCountPossible <= 0) {
            System.out.println("Данное количество переходов недопустимо");
            return;
        }

        String shortCode = service.createShortUrl(originalUrl, accessCountPossible, user.getLogin());
        System.out.println("\nКороткая ссылка создана!");
        System.out.println("Оригинальный URL: " + originalUrl);
        System.out.println("Короткая ссылка: " + service.BASE_URL + shortCode);
        System.out.println("Код: " + shortCode);
    }

    private static void getOriginalUrl() throws UrlNotFoundException, URISyntaxException, IOException {
        System.out.print("Введите короткий код: ");
        String shortCode = scanner.nextLine();
        String originalUrl = service.getOriginalUrl(shortCode, user.getLogin());
        System.out.println("\nПереход по ссылке: " + service.BASE_URL + shortCode);
        Desktop.getDesktop().browse(new URI(originalUrl));
        System.out.println("Оригинальный URL: " + originalUrl);
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число");
            }
        }
    }

    private static User getUserObject() {
        while (true) {
            try {
                System.out.println("Введите логин пользователя");
                User UserObject = User.createOrGetUser(scanner.nextLine());
                return UserObject;
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }


    private static void changeUserObject() {
        user = getUserObject();
    }

}