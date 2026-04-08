package ro.unibuc.pao.service;

import ro.unibuc.pao.model.AuditEntry;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class AuditService {
    private static final AuditService INSTANCE = new AuditService();
    private static final String FILE_NAME = "audit.csv";

    private AuditService() {
    }

    public static AuditService getInstance() {
        return INSTANCE;
    }

    public void logAction(String actionName) {
        AuditEntry entry = new AuditEntry(actionName, LocalDateTime.now());
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(entry.getActionName() + "," + entry.getTimestamp());
        } catch (IOException e) {
            System.out.println("Nu s-a putut scrie in audit.csv");
        }
    }
}
