package ro.unibuc.pao.model;

import java.time.LocalDateTime;

public abstract class Credential {
    private int id;
    private String title;
    private String username;
    private String secret;
    private Category category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected Credential() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    protected Credential(int id, String title, String username, String secret, Category category) {
        this.id = id;
        this.title = title;
        this.username = username;
        this.secret = secret;
        this.category = category;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public abstract String getType();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getDisplayText() {
        return "ID=" + id + ", tip=" + getType() + ", titlu=" + title + ", user=" + username
                + ", categorie=" + (category == null ? "-" : category.getName());
    }
}
