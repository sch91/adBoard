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
        return categoryRepo.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    public Iterable<Category> getListOfCategories() {
        return categoryRepo.findAll();
    }

    public void deleteCategoryById(Long id) {
        categoryRepo.deleteById(id);
    }
}
