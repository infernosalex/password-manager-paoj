package ro.unibuc.pao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseWriteService {
    private static final DatabaseWriteService INSTANCE = new DatabaseWriteService();

    private DatabaseWriteService() {
    }

    public static DatabaseWriteService getInstance() {
        return INSTANCE;
    }

    public void execute(String sql, StatementPreparer preparer) {
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (preparer != null) {
                preparer.prepare(statement);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Scrierea in baza de date a esuat.", e);
        }
    }

    public int executeInsert(String sql, StatementPreparer preparer) {
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            if (preparer != null) {
                preparer.prepare(statement);
            }
            statement.executeUpdate();
            try (var generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Inserarea in baza de date a esuat.", e);
        }
        return 0;
    }
}
