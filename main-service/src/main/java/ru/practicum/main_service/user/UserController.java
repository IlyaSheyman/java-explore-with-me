package ru.practicum.main_service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
import ru.practicum.main_service.event.dto.EventSmallDto;
import ru.practicum.main_service.event.dto.EventUpdateDto;
import ru.practicum.main_service.event_request.dto.EventRequestDto;
import ru.practicum.main_service.event_request.dto.RequestStatusUpdateRequest;
import ru.practicum.main_service.event_request.dto.RequestStatusUpdateResult;
import ru.practicum.main_service.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@Validated
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping("/{id}/events")
    public EventDto createEvent(@PathVariable(value = "id") int userId,
                                @RequestBody @Valid EventCreateDto eventDto) {
        log.info("Получен запрос на добавление нового мероприятия");
        return service.createEvent(userId, eventDto);
    }

    @ResponseBody
    @GetMapping("/{id}/events")
    public List<EventSmallDto> getEventsByInitiator(@PathVariable int id,
                                               @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                               @RequestParam(value = "size", defaultValue = "10") @Min(1) int size) {
        log.info("Получен запрос на получение событий, добавленных текущим пользователем");
        return service.getEventsByUser(id, from, size);
    }

    @ResponseBody
    @GetMapping("/{userId}/events/{eventId}")
    public EventDto getEventByInitiator(@PathVariable int userId,
                                    @PathVariable int eventId) {
        log.info("Получен запрос на получение полной информации о событии, добавленном текущим пользователем");

        return service.getEventByInitiator(userId, eventId);
    }

    @ResponseBody
    @PatchMapping("/{userId}/events/{eventId}")
    public EventDto changeEvent(@PathVariable int userId,
                            @PathVariable int eventId,
                            @RequestBody @Valid EventUpdateDto eventDto) {
        log.info("Получен запрос на изменение события добавленного текущим пользователем");

        return service.changeEvent(userId, eventId, eventDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/requests")
    public EventRequestDto addRequest(@PathVariable int userId,
                                      @RequestParam int eventId) {
        log.info("Получен запрос на создание нового запроса на участие в событии");
        return service.addRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestDto cancelRequest(@PathVariable int userId,
                                         @PathVariable int requestId) {
        log.info("Получен запрос на отмену запроса на участие в событии");
        return service.cancelRequest(userId, requestId);
    }

    @GetMapping("/{userId}/requests")
    public List<EventRequestDto> getAllUserRequests(@PathVariable int userId,
                                   @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                   @RequestParam(value = "size", defaultValue = "10") @Min(1) int size) {
        log.info("Получен запрос на получение информации о заявках текущего пользователя на участие в событиях");
        return service.getAllUserRequests(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<EventRequestDto> getUserRequestsByEvent(@PathVariable int userId,
                                @PathVariable int eventId) {
        log.info("Получен запрос на получение информации о запросах на участие в событии");
        return service.getUserRequestsByEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public RequestStatusUpdateResult changeRequestStatus(@PathVariable int userId,
                                                         @PathVariable int eventId,
                                                         @RequestBody RequestStatusUpdateRequest request) {
        log.info("Получен запрос на изменение статуса заявок на участие в событии текущего пользователя");
        return service.changeRequestStatus(userId, eventId, request);
    }
}