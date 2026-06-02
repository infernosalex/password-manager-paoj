package ro.unibuc.pao.repository;

import ro.unibuc.pao.db.DatabaseReadService;
import ro.unibuc.pao.db.DatabaseWriteService;
import ro.unibuc.pao.model.UserAccount;
import ro.unibuc.pao.model.Vault;

public class VaultRepository {
    private final DatabaseReadService readService = DatabaseReadService.getInstance();
    private final DatabaseWriteService writeService = DatabaseWriteService.getInstance();
    private final UserAccountRepository userAccountRepository = new UserAccountRepository();

    public int insert(Vault vault) {
        int id = writeService.executeInsert(
                "INSERT INTO vaults(name, owner_id) VALUES (?, ?)",
                statement -> {
                    statement.setString(1, vault.getName());
                    statement.setInt(2, vault.getOwner().getId());
                }
        );
        vault.setId(id);
        return id;
    }

    public Vault findByOwnerId(int ownerId) {
        return readService.readOne(
                "SELECT id, name, owner_id FROM vaults WHERE owner_id = ?",
                statement -> statement.setInt(1, ownerId),
                resultSet -> {
                    UserAccount owner = userAccountRepository.findById(resultSet.getInt("owner_id"));
                    return new Vault(resultSet.getInt("id"), resultSet.getString("name"), owner);
                }
        );
    }

    public void update(Vault vault) {
        writeService.execute(
                "UPDATE vaults SET name = ? WHERE id = ?",
                statement -> {
                    statement.setString(1, vault.getName());
                    statement.setInt(2, vault.getId());
                }
        );
    }

    public void delete(int id) {
        writeService.execute(
                "DELETE FROM vaults WHERE id = ?",
                statement -> statement.setInt(1, id)
        );
    }
}
