package ro.unibuc.pao.service;

import ro.unibuc.pao.model.CardCredential;
import ro.unibuc.pao.model.Category;
import ro.unibuc.pao.model.Credential;
import ro.unibuc.pao.model.NoteCredential;
import ro.unibuc.pao.model.SecurityReport;
import ro.unibuc.pao.model.Vault;
import ro.unibuc.pao.model.WebsiteCredential;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CredentialService {
    private int nextId = 1;

    public WebsiteCredential addWebsiteCredential(Vault vault, String title, String username, String password,
                                                  Category category, String websiteUrl, String email,
                                                  String masterPassword) {
        WebsiteCredential credential = new WebsiteCredential(nextId++, title, username,
                SecurityUtils.encrypt(password, masterPassword), category, websiteUrl, email);
        vault.getCredentials().add(credential);
        return credential;
    }

    public CardCredential addCardCredential(Vault vault, String title, String username, String pin,
                                            Category category, String cardHolder, String cardNumber,
                                            String expirationDate, String masterPassword) {
        CardCredential credential = new CardCredential(nextId++, title, username,
                SecurityUtils.encrypt(pin, masterPassword), category, cardHolder,
                SecurityUtils.encrypt(cardNumber, masterPassword), expirationDate);
        vault.getCredentials().add(credential);
        return credential;
    }

    public NoteCredential addNoteCredential(Vault vault, String title, String username, String secret,
                                            Category category, String content, String masterPassword) {
        NoteCredential credential = new NoteCredential(nextId++, title, username,
                SecurityUtils.encrypt(secret, masterPassword), category,
                SecurityUtils.encrypt(content, masterPassword));
        vault.getCredentials().add(credential);
        return credential;
    }

    public List<Credential> getAllCredentials(Vault vault) {
        return new ArrayList<>(vault.getCredentials());
    }

    public Credential findById(Vault vault, int id) {
        for (Credential credential : vault.getCredentials()) {
            if (credential.getId() == id) {
                return credential;
            }
        }
        return null;
    }

    public List<Credential> searchByTitleOrUsername(Vault vault, String text) {
        List<Credential> result = new ArrayList<>();
        for (Credential credential : vault.getCredentials()) {
            if (credential.getTitle().toLowerCase().contains(text.toLowerCase())
                    || credential.getUsername().toLowerCase().contains(text.toLowerCase())) {
                result.add(credential);
            }
        }
        return result;
    }

    public List<Credential> filterByCategory(Vault vault, String categoryName) {
        List<Credential> result = new ArrayList<>();
        for (Credential credential : vault.getCredentials()) {
            if (credential.getCategory() != null
                    && credential.getCategory().getName().equalsIgnoreCase(categoryName)) {
                result.add(credential);
            }
        }
        return result;
    }

    public boolean updateCredential(Vault vault, int id, String newTitle, String newUsername,
                                    String newSecret, String masterPassword) {
        Credential credential = findById(vault, id);
        if (credential == null) {
            return false;
        }

        credential.setTitle(newTitle);
        credential.setUsername(newUsername);
        credential.setSecret(SecurityUtils.encrypt(newSecret, masterPassword));
        credential.touch();
        return true;
    }

    public boolean deleteCredential(Vault vault, int id) {
        Credential credential = findById(vault, id);
        if (credential == null) {
            return false;
        }
        return vault.getCredentials().remove(credential);
    }

    public List<Credential> getSortedCredentials(Vault vault) {
        List<Credential> result = new ArrayList<>(vault.getCredentials());
        result.sort(Comparator.comparing(Credential::getTitle, String.CASE_INSENSITIVE_ORDER));
        return result;
    }

    public String revealSecret(Credential credential, String masterPassword) {
        return SecurityUtils.decrypt(credential.getSecret(), masterPassword);
    }

    public String revealValue(String encryptedValue, String masterPassword) {
        return SecurityUtils.decrypt(encryptedValue, masterPassword);
    }

    public String exportSummary(Vault vault) {
        StringBuilder builder = new StringBuilder();
        for (Credential credential : getSortedCredentials(vault)) {
            builder.append(credential.getDisplayText()).append(System.lineSeparator());
        }
        return builder.toString();
    }

    public SecurityReport buildSecurityReport(Vault vault, String masterPassword) {
        int weakPasswords = 0;
        int duplicateTitles = 0;
        int reusedPasswords = 0;
        Map<String, Integer> titleFrequency = new HashMap<>();
        Map<String, Integer> secretFrequency = new HashMap<>();

        for (Credential credential : vault.getCredentials()) {
            String decryptedSecret = SecurityUtils.decrypt(credential.getSecret(), masterPassword);
            titleFrequency.merge(credential.getTitle().toLowerCase(), 1, Integer::sum);
            secretFrequency.merge(decryptedSecret, 1, Integer::sum);
            if (SecurityUtils.isWeakPassword(decryptedSecret)) {
                weakPasswords++;
            }
        }

        for (Integer value : titleFrequency.values()) {
            if (value > 1) {
                duplicateTitles++;
            }
        }

        for (Integer value : secretFrequency.values()) {
            if (value > 1) {
                reusedPasswords++;
            }
        }

        return new SecurityReport(vault.getCredentials().size(), weakPasswords, reusedPasswords, duplicateTitles);
    }

    public String generatePassword() {
        return SecurityUtils.generatePassword();
    }
}
