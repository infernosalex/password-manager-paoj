package ro.unibuc.pao.model;

import java.util.ArrayList;
import java.util.List;

public class Vault {
    private int id;
    private String name;
    private UserAccount owner;
    private List<Credential> credentials;

    public Vault() {
        this.credentials = new ArrayList<>();
    }

    public Vault(int id, String name, UserAccount owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.credentials = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserAccount getOwner() {
        return owner;
    }

    public void setOwner(UserAccount owner) {
        this.owner = owner;
    }

    public List<Credential> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<Credential> credentials) {
        this.credentials = credentials;
    }
}
