package ru.project.board.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.board.board.entity.Category;
import ru.project.board.board.exception.CategoryNotFoundException;
import ru.project.board.board.repository.CategoryRepo;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    public Category getCategoryById(Long id) throws CategoryNotFoundException {
        Category category = categoryRepo.findById(id).get();
        if (category == null) {
            throw new CategoryNotFoundException("Category not found");
        }
        return category;
    }

    public Iterable<Category> getListOfCategories() {
        return categoryRepo.findAll();
    }
}
