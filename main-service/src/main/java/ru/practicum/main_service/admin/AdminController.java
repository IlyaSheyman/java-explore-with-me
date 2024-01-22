package ru.practicum.main_service.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_service.admin.service.AdminService;
import ru.practicum.main_service.category.model_and_dto.Category;
import ru.practicum.main_service.category.model_and_dto.CategoryDto;
import ru.practicum.main_service.user.model_and_dto.User;
import ru.practicum.main_service.user.model_and_dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/admin")
public class AdminController {

    private final AdminService service;

    @Autowired
    public AdminController(AdminService service) {
        this.service = service;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/categories")
    public Category addCategory(@RequestBody CategoryDto categoryDto) {
        log.info("Получен запрос на добавление категории");
        return service.addCategory(categoryDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/categories/{catId}")
    public void deleteCategory(@PathVariable int catId) {
        log.info("Получен запрос на удаление категории");
        service.deleteCategory(catId);
    }

    @ResponseBody
    @PatchMapping(path = "/categories/{catId}")
    public Category editCategory(@PathVariable int catId,
                                 @RequestBody CategoryDto categoryDto) {
        log.info("Получен запрос на изменение категории");
        return service.editCategory(catId, categoryDto);
    }

    @ResponseBody
    @GetMapping(path = "/users")
    public List<User> getUsers(@RequestParam(value = "ids", required = false) ArrayList<Integer> ids,
                               @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                               @RequestParam(value = "size", defaultValue = "10") @Min(1) int size) {
        log.info("Получен запрос на получение пользователей");
        return service.getUsers(ids, from, size);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/users")
    public User addUser(@RequestBody @Valid UserDto user) {
        log.info("Получен запрос на добавление нового пользователя");
        return service.addUser(user);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/users/{id}")
    public void deleteUser(@PathVariable int id) {
        log.info("Получен запрос на удаление пользователя с id " + id);
        service.deleteUser(id);
    }

    @ResponseBody
    @GetMapping(path = "/events")
    public Object getEvents(@RequestParam(value = "users", required = false) ArrayList<Integer> usersIds,
                            @RequestParam(value = "states") ArrayList<String> states,
                            @RequestParam(value = "categories") ArrayList<Integer> categories,
                            @RequestParam(value = "rangeStart") String rangeStart,
                            @RequestParam(value = "rangeEnd") String rangeEnd,
                            @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size) {
        return null;
    }

}