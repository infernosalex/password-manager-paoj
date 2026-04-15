package ro.unibuc.pao.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final DatabaseManager INSTANCE = new DatabaseManager();
    private static final String DB_PATH = "data/password_manager.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    private DatabaseManager() {
    }

    public static DatabaseManager getInstance() {
        return INSTANCE;
    }

    public void initialize() {
        try {
            Files.createDirectories(Path.of("data"));
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS users (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            username TEXT NOT NULL UNIQUE,
                            master_password_hash TEXT NOT NULL,
                            created_at TEXT NOT NULL
                        )
                        """);
                statement.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS vaults (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT NOT NULL,
                            owner_id INTEGER NOT NULL UNIQUE,
                            FOREIGN KEY (owner_id) REFERENCES users(id)
                        )
                        """);
                statement.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS categories (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT NOT NULL UNIQUE,
                            description TEXT NOT NULL
                        )
                        """);
                statement.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS credentials (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            vault_id INTEGER NOT NULL,
                            type TEXT NOT NULL,
                            title TEXT NOT NULL,
                            username TEXT NOT NULL,
                            secret TEXT NOT NULL,
                            category_id INTEGER,
                            created_at TEXT NOT NULL,
                            updated_at TEXT NOT NULL,
                            extra_one TEXT,
                            extra_two TEXT,
                            extra_three TEXT,
                            FOREIGN KEY (vault_id) REFERENCES vaults(id),
                            FOREIGN KEY (category_id) REFERENCES categories(id)
                        )
                        """);
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            throw new IllegalStateException("Baza de date nu a putut fi initializata.", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
