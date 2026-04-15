package ro.unibuc.pao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseReadService {
    private static final DatabaseReadService INSTANCE = new DatabaseReadService();

    private DatabaseReadService() {
    }

    public static DatabaseReadService getInstance() {
        return INSTANCE;
    }

    public <T> List<T> readList(String sql, StatementPreparer preparer, ResultSetMapper<T> mapper) {
        List<T> result = new ArrayList<>();
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (preparer != null) {
                preparer.prepare(statement);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(mapper.map(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Citirea din baza de date a esuat.", e);
        }
        return result;
    }

    public <T> T readOne(String sql, StatementPreparer preparer, ResultSetMapper<T> mapper) {
        List<T> result = readList(sql, preparer, mapper);
        if (result.isEmpty()) {
            return null;
        }
        return result.getFirst();
    }
}
