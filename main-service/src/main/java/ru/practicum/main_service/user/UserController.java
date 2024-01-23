package ru.practicum.main_service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
import ru.practicum.main_service.event.dto.EventCreateDto;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.user.service.UserService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService userService) {
        this.service = userService ;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping("/{id}/events")
    public EventDto createEvent(@PathVariable(value = "id") int userId,
                                @RequestBody @Valid EventCreateDto eventDto) {
        log.info("Получен запрос на добавление нового мероприятия");
        return service.createEvent(userId, eventDto);
    }
//
//    @GetMapping("/{id}/events")
//    public void untitled() {
//
//    }
//
//    @GetMapping("/{id}/events/{eventId}")
//    public void untitled() {
//
//    }
//
//    @PatchMapping("/{id}/events/{eventId}")
//    public void untitled() {
//
//    }
//
//    @GetMapping("/{id}/events/{eventId}/requests")
//    public void untitled() {
//
//    }
//
//    @PatchMapping("/{id}/events/{eventId}/requests")
//    public void untitled() {
//
//    }
}
