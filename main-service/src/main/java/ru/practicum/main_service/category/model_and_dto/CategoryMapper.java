package ru.practicum.main_service.category.model_and_dto;

import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDto toCategoryDto(Category category) {
        return CategoryDto
                .builder()
                .name(category.getName())
                .build();
    }

    public Category fromCategoryDto(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }
}
