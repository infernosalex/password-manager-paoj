package ro.unibuc.pao.repository;

import ro.unibuc.pao.db.DatabaseReadService;
import ro.unibuc.pao.db.DatabaseWriteService;
import ro.unibuc.pao.model.CardCredential;
import ro.unibuc.pao.model.Category;
import ro.unibuc.pao.model.Credential;
import ro.unibuc.pao.model.NoteCredential;
import ro.unibuc.pao.model.WebsiteCredential;

import java.time.LocalDateTime;
import java.util.List;

public class CredentialRepository {
    private final DatabaseReadService readService = DatabaseReadService.getInstance();
    private final DatabaseWriteService writeService = DatabaseWriteService.getInstance();
    private final CategoryRepository categoryRepository = new CategoryRepository();

    public int insert(int vaultId, Credential credential) {
        int categoryId = credential.getCategory() == null ? 0 : credential.getCategory().getId();
        int id = writeService.executeInsert(
                """
                INSERT INTO credentials(vault_id, type, title, username, secret, category_id, created_at, updated_at,
                extra_one, extra_two, extra_three)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
                statement -> {
                    statement.setInt(1, vaultId);
                    statement.setString(2, credential.getType());
                    statement.setString(3, credential.getTitle());
                    statement.setString(4, credential.getUsername());
                    statement.setString(5, credential.getSecret());
                    if (categoryId == 0) {
                        statement.setNull(6, java.sql.Types.INTEGER);
                    } else {
                        statement.setInt(6, categoryId);
                    }
                    statement.setString(7, credential.getCreatedAt().toString());
                    statement.setString(8, credential.getUpdatedAt().toString());
                    statement.setString(9, getExtraOne(credential));
                    statement.setString(10, getExtraTwo(credential));
                    statement.setString(11, getExtraThree(credential));
                }
        );
        credential.setId(id);
        return id;
    }

    public List<Credential> findByVaultId(int vaultId) {
        return readService.readList(
                "SELECT * FROM credentials WHERE vault_id = ? ORDER BY id",
                statement -> statement.setInt(1, vaultId),
                resultSet -> mapCredential(resultSet.getInt("id"), resultSet.getString("type"),
                        resultSet.getString("title"), resultSet.getString("username"),
                        resultSet.getString("secret"), resultSet.getInt("category_id"),
                        resultSet.getString("created_at"), resultSet.getString("updated_at"),
                        resultSet.getString("extra_one"), resultSet.getString("extra_two"),
                        resultSet.getString("extra_three"))
        );
    }

    public Credential findById(int vaultId, int id) {
        return readService.readOne(
                "SELECT * FROM credentials WHERE vault_id = ? AND id = ?",
                statement -> {
                    statement.setInt(1, vaultId);
                    statement.setInt(2, id);
                },
                resultSet -> mapCredential(resultSet.getInt("id"), resultSet.getString("type"),
                        resultSet.getString("title"), resultSet.getString("username"),
                        resultSet.getString("secret"), resultSet.getInt("category_id"),
                        resultSet.getString("created_at"), resultSet.getString("updated_at"),
                        resultSet.getString("extra_one"), resultSet.getString("extra_two"),
                        resultSet.getString("extra_three"))
        );
    }

    public void update(Credential credential) {
        int categoryId = credential.getCategory() == null ? 0 : credential.getCategory().getId();
        writeService.execute(
                """
                UPDATE credentials
                SET title = ?, username = ?, secret = ?, category_id = ?, updated_at = ?, extra_one = ?, extra_two = ?, extra_three = ?
                WHERE id = ?
                """,
                statement -> {
                    statement.setString(1, credential.getTitle());
                    statement.setString(2, credential.getUsername());
                    statement.setString(3, credential.getSecret());
                    if (categoryId == 0) {
                        statement.setNull(4, java.sql.Types.INTEGER);
                    } else {
                        statement.setInt(4, categoryId);
                    }
                    statement.setString(5, credential.getUpdatedAt().toString());
                    statement.setString(6, getExtraOne(credential));
                    statement.setString(7, getExtraTwo(credential));
                    statement.setString(8, getExtraThree(credential));
                    statement.setInt(9, credential.getId());
                }
        );
    }

    public void delete(int id) {
        writeService.execute(
                "DELETE FROM credentials WHERE id = ?",
                statement -> statement.setInt(1, id)
        );
    }

    private Credential mapCredential(int id, String type, String title, String username, String secret,
                                     int categoryId, String createdAt, String updatedAt,
                                     String extraOne, String extraTwo, String extraThree) {
        Category category = categoryId == 0 ? null : categoryRepository.findById(categoryId);
        Credential credential;
        switch (type) {
            case "WEBSITE" -> credential = new WebsiteCredential(id, title, username, secret, category, extraOne, extraTwo);
            case "CARD" -> credential = new CardCredential(id, title, username, secret, category, extraOne, extraTwo, extraThree);
            case "NOTE" -> credential = new NoteCredential(id, title, username, secret, category, extraOne);
            default -> throw new IllegalStateException("Tip necunoscut: " + type);
        }
        credential.setCreatedAt(LocalDateTime.parse(createdAt));
        credential.setUpdatedAt(LocalDateTime.parse(updatedAt));
        return credential;
    }

    private String getExtraOne(Credential credential) {
        if (credential instanceof WebsiteCredential websiteCredential) {
            return websiteCredential.getWebsiteUrl();
        }
        if (credential instanceof CardCredential cardCredential) {
            return cardCredential.getCardHolder();
        }
        if (credential instanceof NoteCredential noteCredential) {
            return noteCredential.getContent();
        }
        return null;
    }

    private String getExtraTwo(Credential credential) {
        if (credential instanceof WebsiteCredential websiteCredential) {
            return websiteCredential.getEmail();
        }
        if (credential instanceof CardCredential cardCredential) {
            return cardCredential.getCardNumber();
        }
        return null;
    }

    private String getExtraThree(Credential credential) {
        if (credential instanceof CardCredential cardCredential) {
            return cardCredential.getExpirationDate();
        }
        return null;
    }
}
