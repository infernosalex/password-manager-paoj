package ro.unibuc.pao.model;

import java.time.LocalDateTime;

public class UserAccount {
    private int id;
    private String username;
    private String masterPasswordHash;
    private LocalDateTime createdAt;

    public UserAccount() {
    }

    public UserAccount(int id, String username, String masterPasswordHash, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.masterPasswordHash = masterPasswordHash;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMasterPasswordHash() {
        return masterPasswordHash;
    }

    public void setMasterPasswordHash(String masterPasswordHash) {
        this.masterPasswordHash = masterPasswordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
