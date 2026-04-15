package ro.unibuc.pao.service;

import ro.unibuc.pao.model.Category;
import ro.unibuc.pao.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class CategoryService {
    private final CategoryRepository categoryRepository = new CategoryRepository();
    private final TreeSet<Category> sortedCategories = new TreeSet<>();

    public CategoryService() {
        loadCategories();
        if (sortedCategories.isEmpty()) {
            addCategory("Personal", "Conturi personale");
            addCategory("School", "Platforme educationale");
            addCategory("Finance", "Carduri si servicii bancare");
            addCategory("Work", "Conturi de lucru");
        }
    }

    public Category addCategory(String name, String description) {
        Category existingCategory = findByName(name);
        if (existingCategory != null) {
            return existingCategory;
        }

        Category category = new Category(0, name, description);
        categoryRepository.insert(category);
        sortedCategories.add(category);
        return category;
    }

    public Category findById(int id) {
        for (Category category : sortedCategories) {
            if (category.getId() == id) {
                return category;
            }
        }
        return null;
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

    private void loadCategories() {
        sortedCategories.addAll(categoryRepository.findAll());
    }
}
