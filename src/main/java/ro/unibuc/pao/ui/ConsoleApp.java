package ro.unibuc.pao.ui;

import ro.unibuc.pao.db.DatabaseManager;
import ro.unibuc.pao.model.CardCredential;
import ro.unibuc.pao.model.Category;
import ro.unibuc.pao.model.Credential;
import ro.unibuc.pao.model.NoteCredential;
import ro.unibuc.pao.model.SecurityReport;
import ro.unibuc.pao.model.Vault;
import ro.unibuc.pao.model.WebsiteCredential;
import ro.unibuc.pao.service.AuditService;
import ro.unibuc.pao.service.AuthService;
import ro.unibuc.pao.service.CategoryService;
import ro.unibuc.pao.service.CredentialService;

import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private final Scanner scanner;
    private final AuthService authService;
    private final CategoryService categoryService;
    private final CredentialService credentialService;
    private final AuditService auditService;

    public ConsoleApp() {
        DatabaseManager.getInstance().initialize();
        this.scanner = new Scanner(System.in);
        this.authService = new AuthService();
        this.categoryService = new CategoryService();
        this.credentialService = new CredentialService();
        this.auditService = AuditService.getInstance();
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String option = scanner.nextLine();
            switch (option) {
                case "1" -> register();
                case "2" -> login();
                case "3" -> addWebsiteCredential();
                case "4" -> addCardCredential();
                case "5" -> addNoteCredential();
                case "6" -> listCredentials();
                case "7" -> searchCredentials();
                case "8" -> filterByCategory();
                case "9" -> updateCredential();
                case "10" -> deleteCredential();
                case "11" -> sortCredentials();
                case "12" -> showSecurityReport();
                case "13" -> showCategories();
                case "14" -> addCategory();
                case "15" -> generatePassword();
                case "16" -> exportSummary();
                case "17" -> logout();
                case "18" -> updateCategory();
                case "19" -> deleteCategory();
                case "20" -> updateAccount();
                case "21" -> renameVault();
                case "22" -> deleteAccount();
                case "0" -> running = false;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("=== Password Manager ===");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Add website credential");
        System.out.println("4. Add card credential");
        System.out.println("5. Add secure note");
        System.out.println("6. List all entries");
        System.out.println("7. Search entry");
        System.out.println("8. Filter by category");
        System.out.println("9. Update entry");
        System.out.println("10. Delete entry");
        System.out.println("11. Sort entries");
        System.out.println("12. Security report");
        System.out.println("13. Show categories");
        System.out.println("14. Add category");
        System.out.println("15. Generate password");
        System.out.println("16. Export summary");
        System.out.println("17. Logout");
        System.out.println("18. Update category");
        System.out.println("19. Delete category");
        System.out.println("20. Update account");
        System.out.println("21. Rename vault");
        System.out.println("22. Delete account");
        System.out.println("0. Exit");
        System.out.print("Alege optiunea: ");
    }

    private void register() {
        auditService.logAction("register");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Master password: ");
        String password = scanner.nextLine();

        if (authService.register(username, password)) {
            System.out.println("Cont creat.");
        } else {
            System.out.println("Exista deja un cont cu acest username.");
        }
    }

    private void login() {
        auditService.logAction("login");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Master password: ");
        String password = scanner.nextLine();

        if (authService.login(username, password)) {
            System.out.println("Autentificare reusita.");
        } else {
            System.out.println("Date invalide.");
        }
    }

    private void addWebsiteCredential() {
        if (!checkAuthentication()) {
            return;
        }

        auditService.logAction("addWebsiteCredential");
        Category category = readCategory();
        System.out.print("Titlu: ");
        String title = scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Parola: ");
        String password = scanner.nextLine();
        System.out.print("Website url: ");
        String url = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        WebsiteCredential credential = credentialService.addWebsiteCredential(getCurrentVault(), title, username,
                password, category, url, email, authService.getCurrentMasterPassword());
        System.out.println("Intrare website adaugata cu id " + credential.getId());
    }

    private void addCardCredential() {
        if (!checkAuthentication()) {
            return;
        }

        auditService.logAction("addCardCredential");
        Category category = readCategory();
        System.out.print("Titlu: ");
        String title = scanner.nextLine();
        System.out.print("Username asociat: ");
        String username = scanner.nextLine();
        System.out.print("PIN: ");
        String pin = scanner.nextLine();
        System.out.print("Titular card: ");
        String cardHolder = scanner.nextLine();
        System.out.print("Numar card: ");
        String cardNumber = scanner.nextLine();
        System.out.print("Data expirare: ");
        String expirationDate = scanner.nextLine();

        CardCredential credential = credentialService.addCardCredential(getCurrentVault(), title, username, pin,
                category, cardHolder, cardNumber, expirationDate, authService.getCurrentMasterPassword());
        System.out.println("Intrare card adaugata cu id " + credential.getId());
    }

    private void addNoteCredential() {
        if (!checkAuthentication()) {
            return;
        }

        auditService.logAction("addNoteCredential");
        Category category = readCategory();
        System.out.print("Titlu: ");
        String title = scanner.nextLine();
        System.out.print("Username asociat: ");
        String username = scanner.nextLine();
        System.out.print("Secret scurt: ");
        String secret = scanner.nextLine();
        System.out.print("Continut nota: ");
        String content = scanner.nextLine();

        NoteCredential credential = credentialService.addNoteCredential(getCurrentVault(), title, username, secret,
                category, content, authService.getCurrentMasterPassword());
        System.out.println("Nota securizata adaugata cu id " + credential.getId());
    }

    private void listCredentials() {
        if (!checkAuthentication()) {
            return;
        }

        auditService.logAction("listCredentials");
        List<Credential> credentials = credentialService.getAllCredentials(getCurrentVault());
        printCredentials(credentials);
    }

    private void searchCredentials() {
        if (!checkAuthentication()) {
            return;
        }

        auditService.logAction("searchCredentials");
        System.out.print("Text cautat: ");
        String text = scanner.nextLine();
        List<Credential> credentials = credentialService.searchByTitleOrUsername(getCurrentVault(), text);
        printCredentials(credentials);
    }

    private void filterByCategory() {
        if (!checkAuthentication()) {
            return;
        }

        auditService.logAction("filterByCategory");
        System.out.print("Categorie: ");
        String categoryName = scanner.nextLine();
        List<Credential> credentials = credentialService.filterByCategory(getCurrentVault(), categoryName);
        printCredentials(credentials);
    }

    private void updateCredential() {
        if (!checkAuthentication()) {
            return;
        }

        auditService.logAction("updateCredential");
        System.out.print("ID intrare: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Titlu nou: ");
        String title = scanner.nextLine();
        System.out.print("Username nou: ");
        String username = scanner.nextLine();
        System.out.print("Secret nou: ");
        String secret = scanner.nextLine();

        boolean updated = credentialService.updateCredential(getCurrentVault(), id, title, username, secret,
                authService.getCurrentMasterPassword());
        System.out.println(updated ? "Intrare actualizata." : "Nu exista intrarea ceruta.");
    }

    private void deleteCredential() {
        if (!checkAuthentication()) {
            return;
        }

        auditService.logAction("deleteCredential");
        System.out.print("ID intrare: ");
        int id = Integer.parseInt(scanner.nextLine());
        boolean deleted = credentialService.deleteCredential(getCurrentVault(), id);
        System.out.println(deleted ? "Intrare stearsa." : "Nu exista intrarea ceruta.");
    }

    private void sortCredentials() {
        if (!checkAuthentication()) {
            return;
        }

        auditService.logAction("sortCredentials");
        List<Credential> credentials = credentialService.getSortedCredentials(getCurrentVault());
        printCredentials(credentials);
    }

    private void showSecurityReport() {
        if (!checkAuthentication()) {
            return;
        }

        auditService.logAction("showSecurityReport");
        SecurityReport report = credentialService.buildSecurityReport(getCurrentVault(),
                authService.getCurrentMasterPassword());
        System.out.println(report);
    }

    private void showCategories() {
        auditService.logAction("showCategories");
        printCategories();
    }

    private void printCategories() {
        for (Category category : categoryService.getAllCategories()) {
            System.out.println(category.getId() + ". " + category);
        }
    }

    private void addCategory() {
        if (!checkAuthentication()) {
            return;
        }

        auditService.logAction("addCategory");
        System.out.print("Nume categorie: ");
        String name = scanner.nextLine();
        System.out.print("Descriere: ");
        String description = scanner.nextLine();
        Category category = categoryService.addCategory(name, description);
        System.out.println("Categorie salvata cu id " + category.getId());
    }

    private void generatePassword() {
        auditService.logAction("generatePassword");
        System.out.println("Parola generata: " + credentialService.generatePassword());
    }

    private void exportSummary() {
        if (!checkAuthentication()) {
            return;
        }

        auditService.logAction("exportSummary");
        System.out.println(credentialService.exportSummary(getCurrentVault()));
    }

    private void logout() {
        auditService.logAction("logout");
        authService.logout();
        System.out.println("Logout realizat.");
    }

    private void updateCategory() {
        auditService.logAction("updateCategory");
        printCategories();
        System.out.print("ID categorie: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Nume nou: ");
        String name = scanner.nextLine();
        System.out.print("Descriere noua: ");
        String description = scanner.nextLine();
        boolean updated = categoryService.updateCategory(id, name, description);
        System.out.println(updated ? "Categorie actualizata." : "Nu exista categoria ceruta.");
    }

    private void deleteCategory() {
        auditService.logAction("deleteCategory");
        printCategories();
        System.out.print("ID categorie: ");
        int id = Integer.parseInt(scanner.nextLine());
        boolean deleted = categoryService.deleteCategory(id);
        System.out.println(deleted ? "Categorie stearsa." : "Nu exista categoria ceruta.");
    }

    private void updateAccount() {
        if (!checkAuthentication()) {
            return;
        }

        auditService.logAction("updateAccount");
        System.out.print("Username nou: ");
        String username = scanner.nextLine();
        System.out.print("Master password nou: ");
        String password = scanner.nextLine();
        boolean updated = authService.updateAccount(username, password);
        System.out.println(updated ? "Cont actualizat." : "Nu s-a putut actualiza contul.");
    }

    private void renameVault() {
        if (!checkAuthentication()) {
            return;
        }

        auditService.logAction("renameVault");
        System.out.print("Nume nou seif: ");
        String name = scanner.nextLine();
        boolean updated = authService.renameVault(name);
        System.out.println(updated ? "Seif redenumit." : "Nu s-a putut redenumi seiful.");
    }

    private void deleteAccount() {
        if (!checkAuthentication()) {
            return;
        }

        auditService.logAction("deleteAccount");
        System.out.print("Scrie DA pentru a confirma stergerea contului: ");
        String confirmation = scanner.nextLine();
        if (!confirmation.equals("DA")) {
            System.out.println("Stergere anulata.");
            return;
        }
        boolean deleted = authService.deleteAccount();
        System.out.println(deleted ? "Cont sters." : "Nu s-a putut sterge contul.");
    }

    private boolean checkAuthentication() {
        if (!authService.isAuthenticated()) {
            System.out.println("Trebuie sa fii autentificat.");
            return false;
        }
        return true;
    }

    private Category readCategory() {
        printCategories();
        System.out.print("Alege id categorie: ");
        int categoryId = Integer.parseInt(scanner.nextLine());
        Category category = categoryService.findById(categoryId);
        if (category == null) {
            return categoryService.getAllCategories().getFirst();
        }
        return category;
    }

    private void printCredentials(List<Credential> credentials) {
        if (credentials.isEmpty()) {
            System.out.println("Nu exista rezultate.");
            return;
        }

        for (Credential credential : credentials) {
            System.out.println(credential.getDisplayText());
            System.out.println("Secret: " + credentialService.revealSecret(credential, authService.getCurrentMasterPassword()));
            if (credential instanceof CardCredential cardCredential) {
                System.out.println("Numar card: " + credentialService.revealValue(cardCredential.getCardNumber(),
                        authService.getCurrentMasterPassword()));
            }
            if (credential instanceof NoteCredential noteCredential) {
                System.out.println("Nota decriptata: " + credentialService.revealValue(noteCredential.getContent(),
                        authService.getCurrentMasterPassword()));
            }
        }
    }

    private Vault getCurrentVault() {
        return authService.getCurrentVault();
    }
}
