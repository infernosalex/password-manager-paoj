package ro.unibuc.pao.model;

import java.time.LocalDateTime;

public class AuditEntry {
    private String actionName;
    private LocalDateTime timestamp;

    public AuditEntry() {
    }

    public AuditEntry(String actionName, LocalDateTime timestamp) {
        this.actionName = actionName;
        this.timestamp = timestamp;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
