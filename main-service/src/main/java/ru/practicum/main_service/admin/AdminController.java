package ru.practicum.main_service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
import ru.practicum.main_service.compilation.dto.CompilationBigDto;
import ru.practicum.main_service.compilation.dto.NewCompilationDto;
import ru.practicum.main_service.compilation.dto.UpdateCompilationDto;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.event.dto.EventUpdateAdminDto;
import ru.practicum.main_service.user.model_and_dto.User;
import ru.practicum.main_service.user.model_and_dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.main_service.consts.Consts.TIME_FORMAT;


@Slf4j
@RequestMapping(path = "/admin")
@Validated
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService service;

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/categories")
    public Category addCategory(@RequestBody @Valid CategoryDto categoryDto) {
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
                                 @RequestBody @Valid CategoryDto categoryDto) {
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
    public List<EventDto> getEvents(@RequestParam(value = "users", required = false) ArrayList<Integer> usersIds,
                                    @RequestParam(value = "states", required = false) ArrayList<String> states,
                                    @RequestParam(value = "categories", required = false) ArrayList<Integer> categories,
                                    @RequestParam(value = "rangeStart", required = false)
                                        @DateTimeFormat(pattern = TIME_FORMAT) LocalDateTime rangeStart,
                                    @RequestParam(value = "rangeEnd", required = false)
                                        @DateTimeFormat(pattern = TIME_FORMAT) LocalDateTime rangeEnd,
                                    @RequestParam(value = "from", defaultValue = "0", required = false) @Min(0) int from,
                                    @RequestParam(value = "size", defaultValue = "10", required = false) @Min(1) int size) {
        log.info("Получен запрос на получение полной информации обо всех событиях");

        return service.getEvents(usersIds,states, categories, rangeStart, rangeEnd, from, size);
    }

    @ResponseBody
    @PatchMapping(path = "/events/{eventId}")
    public EventDto eventAdministration(@PathVariable int eventId,
                                        @RequestBody @Valid EventUpdateAdminDto updateAdminDto) {
        log.info("Получен запрос на администрацию события");
        return service.eventAdministration(eventId, updateAdminDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/compilations/{compId}")
    public void deleteCompilation(@PathVariable int compId) {
        log.info("Получен запрос на удаление подборки событий");
        service.deleteCompilation(compId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/compilations")
    public CompilationBigDto addCompilation(@RequestBody @Valid NewCompilationDto dto) {
        log.info("Получен запрос на добавление новой подборки событий");
        return service.addCompilation(dto);
    }

    @PatchMapping(path = "/compilations/{compId}")
    public CompilationBigDto updateCompilation(@PathVariable int compId,
                                               @RequestBody @Valid UpdateCompilationDto dto) {
        log.info("Получен запрос на обновление подборки событий");
        return service.updateCompilation(compId, dto);
    }
}