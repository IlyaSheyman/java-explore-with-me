package ru.practicum.main_service.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_service.category.model_and_dto.Category;
import ru.practicum.main_service.category.service.CategoryService;

import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
public class CategoryController {
    private final CategoryService service;

    @Autowired
    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @ResponseBody
    @GetMapping
    public List<Category> getCategories(@RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                        @RequestParam(value = "size", defaultValue = "10") @Min(1) int size) {
        log.info("Поступил запрос на получение всех категорий");
        return service.getCategories(from, size);
    }

    @ResponseBody
    @GetMapping(path = "/{id}")
    public Category getCategoryById(@PathVariable int id) {
        log.info("Поступил запрос на получение категории по id");
        return service.getCategoriesById(id);
    }

}