package ru.practicum.main_service.category.model_and_dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {
    String name;

    public CategoryDto(String name) {
        this.name = name;
    }
}