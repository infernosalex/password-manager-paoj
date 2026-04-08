package ro.unibuc.pao.service;

import ro.unibuc.pao.model.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class CategoryService {
    private final TreeSet<Category> sortedCategories = new TreeSet<>();
    private final Map<Integer, Category> categoriesById = new HashMap<>();
    private int nextId = 1;

    public CategoryService() {
        addCategory("Personal", "Conturi personale");
        addCategory("School", "Platforme educationale");
        addCategory("Finance", "Carduri si servicii bancare");
        addCategory("Work", "Conturi de lucru");
    }

    public Category addCategory(String name, String description) {
        Category existingCategory = findByName(name);
        if (existingCategory != null) {
            return existingCategory;
        }

        Category category = new Category(nextId++, name, description);
        sortedCategories.add(category);
        categoriesById.put(category.getId(), category);
        return category;
    }

    public Category findById(int id) {
        return categoriesById.get(id);
    }

    public Category findByName(String name) {
        for (Category category : sortedCategories) {
            if (category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }

    public List<Category> getAllCategories() {
        return new ArrayList<>(sortedCategories);
    }
}
