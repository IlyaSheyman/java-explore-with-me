package ru.practicum.main_service.category.service;

import ru.practicum.main_service.category.model_and_dto.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getCategories(int from, int size);

    Category getCategoriesById(int id);
}
