package ro.unibuc.pao.service;

import ro.unibuc.pao.model.UserAccount;
import ro.unibuc.pao.model.Vault;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private final Map<String, UserAccount> usersByUsername = new HashMap<>();
    private final Map<Integer, Vault> vaultsByUserId = new HashMap<>();
    private int nextUserId = 1;
    private int nextVaultId = 1;
    private UserAccount currentUser;
    private String currentMasterPassword;

    public boolean register(String username, String masterPassword) {
        if (usersByUsername.containsKey(username)) {
            return false;
        }

        UserAccount user = new UserAccount(nextUserId++, username,
                SecurityUtils.hash(masterPassword), LocalDateTime.now());
        Vault vault = new Vault(nextVaultId++, username + "_vault", user);

        usersByUsername.put(username, user);
        vaultsByUserId.put(user.getId(), vault);
        return true;
    }

    public boolean login(String username, String masterPassword) {
        UserAccount user = usersByUsername.get(username);
        if (user == null) {
            return false;
        }

        if (!SecurityUtils.matches(masterPassword, user.getMasterPasswordHash())) {
            return false;
        }

        currentUser = user;
        currentMasterPassword = masterPassword;
        return true;
    }

    public void logout() {
        currentUser = null;
        currentMasterPassword = null;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public UserAccount getCurrentUser() {
        return currentUser;
    }

    public Vault getCurrentVault() {
        if (currentUser == null) {
            return null;
        }
        return vaultsByUserId.get(currentUser.getId());
    }

    public String getCurrentMasterPassword() {
        return currentMasterPassword;
    }

    public Map<String, UserAccount> getUsersByUsername() {
        return usersByUsername;
    }

    public Map<Integer, Vault> getVaultsByUserId() {
        return vaultsByUserId;
    }
}
