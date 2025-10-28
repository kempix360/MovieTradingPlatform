package com.app.movietradingplatform.entity.category.view;

import com.app.movietradingplatform.entity.category.Category;
import com.app.movietradingplatform.entity.category.service.CategoryService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

import java.util.List;

@Named
@RequestScoped
public class CategoryListView {

    @Inject
    private CategoryService categoryService;

    @Getter
    private List<Category<?>> categories;

    @PostConstruct
    public void init() {
        categories = categoryService.getAllCategories();
    }

    public String deleteCategory(String name) {
        categoryService.deleteCategory(name);
        return "/view/category/category_list.xhtml?faces-redirect=true";
    }

    public String getCategoryPage(String categoryName) {
        switch (categoryName.toLowerCase()) {
            case "users":
                return "/view/user/user_list.xhtml";
            case "directors":
                return "/view/director/director_list.xhtml";
            default:
                throw new IllegalArgumentException("Unknown category: " + categoryName);
        }
    }
}
