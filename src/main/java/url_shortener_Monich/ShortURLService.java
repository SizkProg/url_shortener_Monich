package url_shortener_Monich;

import url_shortener_Monich.exceptions.InvalidUrlException;
import url_shortener_Monich.exceptions.UrlDeleteException;
import url_shortener_Monich.exceptions.UrlNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class ShortURLService {

    private final Map<String, ShortUrl> ShortUrlMap;
    private final Random random;
    private static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int SHORT_CODE_LENGTH = 5;
    public final String BASE_URL = "http://short.url/";

    public ShortURLService() {
        this.ShortUrlMap =  new HashMap<>();
        this.random = new Random();
    }

    public String createShortUrl(String originalUrl, int accessCountPossible, String UserLogin) throws InvalidUrlException {

        validateUrl(originalUrl);

        Optional<ShortUrl> existing = ShortUrlMap.values().stream()
                .filter(url -> url.getOriginalUrl().equals(originalUrl) &&
                        url.getUsername().equals(UserLogin))
                .findFirst();


        if (existing.isPresent()) {
            return existing.get().getShortCode();
        }

        String shortCode = generateShortCode();
        ShortUrl shortUrl = new ShortUrl(shortCode, originalUrl, UserLogin, accessCountPossible);
        ShortUrlMap.put(shortCode, shortUrl);
        return shortCode;
    }

    public String getOriginalUrl(String shortCode, String UserLogin) throws UrlNotFoundException, UrlDeleteException {
        ShortUrl shortUrl = ShortUrlMap.get(shortCode);
        if (shortUrl == null && shortUrl.getUsername() == UserLogin) {
            throw new UrlNotFoundException("Короткая ссылка не найдена: " + shortCode);
        }
        String Massage = shortUrl.incrementAccessCount();
        if (shortUrl.isDelete()){
            System.out.println(Massage + ", ссылка будет удалена");
            ShortUrlMap.remove(shortCode);
        }
        return shortUrl.getOriginalUrl();
    }


    private void validateUrl(String url) throws InvalidUrlException {
        if (url == null || url.trim().isEmpty()) {
            throw new InvalidUrlException("URL не может быть пустым");
        }
        if (!url.matches("^https?://.*")) {
            throw new InvalidUrlException("URL должен начинаться с http:// или https://");
        }

        if (url.length() > 2048) {
            throw new InvalidUrlException("URL слишком длинный (максимум 2048 символов)");
        }
    }

    private String generateShortCode() {
        String shortCode;
        do {
            StringBuilder sb = new StringBuilder(SHORT_CODE_LENGTH);
            for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
                sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
            }
            shortCode = sb.toString();
        } while (ShortUrlMap.containsKey(shortCode));
        return shortCode;
    }

    public void showAllUrls(String UserLogin) {
        if (ShortUrlMap.isEmpty()) {
            System.out.println("Нет сохраненных ссылок");
            return;
        }

        List<ShortUrl> userUrls = ShortUrlMap.values().stream()
                .filter(url -> url.getUsername().equals(UserLogin))
                .sorted(Comparator.comparing(ShortUrl::getCreatedAt).reversed())
                .collect(Collectors.toList());

        if (userUrls.isEmpty()) {
            System.out.println("У пользователя '" + UserLogin + "' нет сохраненных ссылок");
            return;
        }

        System.out.println("\n=== Ссылки пользователя: " + UserLogin + " ===");
        userUrls.forEach(System.out::println);
    }

}
