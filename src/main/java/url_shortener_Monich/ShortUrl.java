package url_shortener_Monich;

import java.time.LocalDateTime;
import java.util.Objects;

public class ShortUrl {
    private final String shortCode;
    private final String originalUrl;
    private final LocalDateTime createdAt;
    private final String username;
    private int accessCount;
    private int accessCountPossible;
    private boolean delete;
    private final LocalDateTime createdAtPossible;

    public ShortUrl(String shortCode, String originalUrl, String username, int accessCountPossible) {
        this.accessCount = 0;
        this.username = username;
        this.createdAt = LocalDateTime.now();
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.accessCountPossible = accessCountPossible;
        this.delete = false;
        this.createdAtPossible = LocalDateTime.now().plusMinutes(30) ;
    }


    public String getShortCode() {
        return shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getUsername() {
        return username;
    }

    public int getAccessCount() {
        return accessCount;
    }

    public String incrementAccessCount() {
        accessCount++;
        if (accessCount == accessCountPossible) {
            delete = true;
            return "Исчерпан лимит";
        }
        if (LocalDateTime.now().isAfter(createdAtPossible)){
            delete = true;
            return "Время жизни истекло";
        }
        return "";
    }

    public boolean isDelete() {
        return delete;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShortUrl shortUrl = (ShortUrl) o;
        return Objects.equals(username.trim().toLowerCase(), shortUrl.username.trim().toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username.trim().toLowerCase());
    }

    @Override
    public String toString() {
        return shortCode + " -> " + originalUrl + " (переходов: " + accessCount + ", количество возможных переходов: " + accessCountPossible + ", " +
                "\n создан: " + createdAt + ", время жизни ссылки закончит в " + createdAtPossible + " пользователем " + username + ")";
    }
}
