package com.app.movietradingplatform.entity.category.service;

import com.app.movietradingplatform.entity.category.Category;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class CategoryService {

    @Inject
    private UserService userService;

    @Inject
    private DirectorService directorService;

    public List<Category<?>> getAllCategories() {
        List<Category<?>> categories = new ArrayList<>();
        categories.add(new Category<>("Users", userService.getAll()));
        categories.add(new Category<>("Directors", directorService.getAll()));

        return categories.stream()
                .filter(category -> category.getElements() != null && !category.getElements().isEmpty())
                .collect(Collectors.toList());
    }

    public Category<?> findByName(String name) {
        return getAllCategories().stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void deleteCategory(String name) {
        switch (name.toLowerCase()) {
            case "users":
                userService.deleteAll();
                break;
            case "directors":
                directorService.deleteAll();
                break;
            default:
                throw new IllegalArgumentException("Unknown category: " + name);
        }
    }
}
