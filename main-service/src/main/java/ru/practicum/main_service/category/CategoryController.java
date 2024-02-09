package ru.practicum.main_service.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_service.category.model_and_dto.CategoryBigDto;
import ru.practicum.main_service.category.model_and_dto.CategoryMapper;
import ru.practicum.main_service.category.service.CategoryService;
import ru.practicum.main_service.category.service.CategoryServiceImpl;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
public class CategoryController {

    @Qualifier("CategoryServiceImpl")
    private final CategoryService service;
    private final CategoryMapper mapper;

    @Autowired
    public CategoryController(CategoryServiceImpl service, CategoryMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @ResponseBody
    @GetMapping
    public List<CategoryBigDto> getCategories(@RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                        @RequestParam(value = "size", defaultValue = "10") @Min(1) int size) {
        log.info("Request to get all categories has been received. From: {}, Size: {}", from, size);

        return service.getCategories(from, size).stream().map(mapper::toCategoryBigDto).collect(Collectors.toList());
    }

    @ResponseBody
    @GetMapping(path = "/{id}")
    public CategoryBigDto getCategoryById(@PathVariable int id) {
        log.info("Request to get category by id has been received. CategoryId: {}", id);

        return mapper.toCategoryBigDto(service.getCategoriesById(id));
    }
}