package ro.unibuc.pao.model;

public class SecurityReport {
    private int totalEntries;
    private int weakPasswords;
    private int reusedPasswords;
    private int duplicateTitles;

    public SecurityReport() {
    }

    public SecurityReport(int totalEntries, int weakPasswords, int reusedPasswords, int duplicateTitles) {
        this.totalEntries = totalEntries;
        this.weakPasswords = weakPasswords;
        this.reusedPasswords = reusedPasswords;
        this.duplicateTitles = duplicateTitles;
    }

    public int getTotalEntries() {
        return totalEntries;
    }

    public void setTotalEntries(int totalEntries) {
        this.totalEntries = totalEntries;
    }

    public int getWeakPasswords() {
        return weakPasswords;
    }

    public void setWeakPasswords(int weakPasswords) {
        this.weakPasswords = weakPasswords;
    }

    public int getReusedPasswords() {
        return reusedPasswords;
    }

    public void setReusedPasswords(int reusedPasswords) {
        this.reusedPasswords = reusedPasswords;
    }

    public int getDuplicateTitles() {
        return duplicateTitles;
    }

    public void setDuplicateTitles(int duplicateTitles) {
        this.duplicateTitles = duplicateTitles;
    }

    @Override
    public String toString() {
        return "Total intrari: " + totalEntries
                + ", parole slabe: " + weakPasswords
                + ", parole reutilizate: " + reusedPasswords
                + ", titluri duplicate: " + duplicateTitles;
    }
}
