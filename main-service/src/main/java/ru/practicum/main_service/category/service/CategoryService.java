package ru.practicum.main_service.category.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.category.model_and_dto.Category;
import ru.practicum.main_service.category.model_and_dto.CategoryMapper;
import ru.practicum.main_service.category.storage.CategoryRepository;
import ru.practicum.main_service.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
public class CategoryService {
    private final CategoryRepository repository;

    private final CategoryMapper mapper;

    public CategoryService(CategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<Category> getCategories(int from, int size) {
        Page<Category> categories = repository.findAll(PageRequest.of(from / size, size));
        return categories.getContent();
    }


    public Category getCategoriesById(int id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с id " + id + " не найдена"));
    }
}