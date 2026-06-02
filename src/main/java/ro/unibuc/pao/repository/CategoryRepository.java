package ro.unibuc.pao.repository;

import ro.unibuc.pao.db.DatabaseReadService;
import ro.unibuc.pao.db.DatabaseWriteService;
import ro.unibuc.pao.model.Category;

import java.util.List;

public class CategoryRepository {
    private final DatabaseReadService readService = DatabaseReadService.getInstance();
    private final DatabaseWriteService writeService = DatabaseWriteService.getInstance();

    public int insert(Category category) {
        int id = writeService.executeInsert(
                "INSERT INTO categories(name, description) VALUES (?, ?)",
                statement -> {
                    statement.setString(1, category.getName());
                    statement.setString(2, category.getDescription());
                }
        );
        category.setId(id);
        return id;
    }

    public List<Category> findAll() {
        return readService.readList(
                "SELECT id, name, description FROM categories ORDER BY LOWER(name)",
                null,
                resultSet -> new Category(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description")
                )
        );
    }

    public Category findById(int id) {
        return readService.readOne(
                "SELECT id, name, description FROM categories WHERE id = ?",
                statement -> statement.setInt(1, id),
                resultSet -> new Category(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description")
                )
        );
    }

    public Category findByName(String name) {
        return readService.readOne(
                "SELECT id, name, description FROM categories WHERE LOWER(name) = LOWER(?)",
                statement -> statement.setString(1, name),
                resultSet -> new Category(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description")
                )
        );
    }

    public void update(Category category) {
        writeService.execute(
                "UPDATE categories SET name = ?, description = ? WHERE id = ?",
                statement -> {
                    statement.setString(1, category.getName());
                    statement.setString(2, category.getDescription());
                    statement.setInt(3, category.getId());
                }
        );
    }

    public void delete(int id) {
        writeService.execute(
                "DELETE FROM categories WHERE id = ?",
                statement -> statement.setInt(1, id)
        );
    }
}
