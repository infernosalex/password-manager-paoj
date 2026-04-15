package ro.unibuc.pao.service;

import ro.unibuc.pao.model.UserAccount;
import ro.unibuc.pao.model.Vault;
import ro.unibuc.pao.repository.UserAccountRepository;
import ro.unibuc.pao.repository.VaultRepository;

import java.time.LocalDateTime;

public class AuthService {
    private final UserAccountRepository userAccountRepository = new UserAccountRepository();
    private final VaultRepository vaultRepository = new VaultRepository();
    private UserAccount currentUser;
    private Vault currentVault;
    private String currentMasterPassword;

    public boolean register(String username, String masterPassword) {
        if (userAccountRepository.findByUsername(username) != null) {
            return false;
        }

        UserAccount user = new UserAccount(0, username,
                SecurityUtils.hash(masterPassword), LocalDateTime.now());
        userAccountRepository.insert(user);

        Vault vault = new Vault(0, username + "_vault", user);
        vaultRepository.insert(vault);
        return true;
    }

    public boolean login(String username, String masterPassword) {
        UserAccount user = userAccountRepository.findByUsername(username);
        if (user == null) {
            return false;
        }

        if (!SecurityUtils.matches(masterPassword, user.getMasterPasswordHash())) {
            return false;
        }

        currentUser = user;
        currentVault = vaultRepository.findByOwnerId(user.getId());
        currentMasterPassword = masterPassword;
        return true;
    }

    public void logout() {
        currentUser = null;
        currentVault = null;
        currentMasterPassword = null;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public UserAccount getCurrentUser() {
        return currentUser;
    }

    public Vault getCurrentVault() {
        return currentVault;
    }

    public String getCurrentMasterPassword() {
        return currentMasterPassword;
    }
}
