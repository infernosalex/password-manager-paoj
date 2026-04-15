package ro.unibuc.pao.repository;

import ro.unibuc.pao.db.DatabaseReadService;
import ro.unibuc.pao.db.DatabaseWriteService;
import ro.unibuc.pao.model.UserAccount;

import java.time.LocalDateTime;
import java.util.List;

public class UserAccountRepository {
    private final DatabaseReadService readService = DatabaseReadService.getInstance();
    private final DatabaseWriteService writeService = DatabaseWriteService.getInstance();

    public int insert(UserAccount userAccount) {
        int id = writeService.executeInsert(
                "INSERT INTO users(username, master_password_hash, created_at) VALUES (?, ?, ?)",
                statement -> {
                    statement.setString(1, userAccount.getUsername());
                    statement.setString(2, userAccount.getMasterPasswordHash());
                    statement.setString(3, userAccount.getCreatedAt().toString());
                }
        );
        userAccount.setId(id);
        return id;
    }

    public UserAccount findByUsername(String username) {
        return readService.readOne(
                "SELECT id, username, master_password_hash, created_at FROM users WHERE username = ?",
                statement -> statement.setString(1, username),
                resultSet -> new UserAccount(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("master_password_hash"),
                        LocalDateTime.parse(resultSet.getString("created_at"))
                )
        );
    }

    public UserAccount findById(int id) {
        return readService.readOne(
                "SELECT id, username, master_password_hash, created_at FROM users WHERE id = ?",
                statement -> statement.setInt(1, id),
                resultSet -> new UserAccount(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("master_password_hash"),
                        LocalDateTime.parse(resultSet.getString("created_at"))
                )
        );
    }

    public List<UserAccount> findAll() {
        return readService.readList(
                "SELECT id, username, master_password_hash, created_at FROM users ORDER BY id",
                null,
                resultSet -> new UserAccount(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("master_password_hash"),
                        LocalDateTime.parse(resultSet.getString("created_at"))
                )
        );
    }
}
