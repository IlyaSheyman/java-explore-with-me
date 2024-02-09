package ru.practicum.main_service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
import ru.practicum.main_service.event.request.dto.EventRequestDto;
import ru.practicum.main_service.event.request.dto.RequestStatusUpdateRequest;
import ru.practicum.main_service.event.request.dto.RequestStatusUpdateResult;
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

    @Qualifier("UserServiceImpl")
    private final UserService service;

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping("/{id}/events")
    public EventDto createEvent(@PathVariable(value = "id") int userId,
                                @RequestBody @Valid EventCreateDto eventDto) {
        log.info("Request to create event has been received. UserId: {}, Event dto: {}", userId, eventDto.toString());

        return service.createEvent(userId, eventDto);
    }

    @ResponseBody
    @GetMapping("/{id}/events")
    public List<EventSmallDto> getEventsByInitiator(@PathVariable int id,
                                                    @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                                    @RequestParam(value = "size", defaultValue = "10") @Min(1) int size) {
        log.info("Request to get events added by the initiator has been received. InitiatorId: {}. From: {}, Size: {}",
                id, from, size);

        return service.getEventsByUser(id, from, size);
    }

    @ResponseBody
    @GetMapping("/{userId}/events/{eventId}")
    public EventDto getEventByInitiator(@PathVariable int userId,
                                        @PathVariable int eventId) {
        log.info("Request to get complete information about an event added by the current user has been received. " +
                "UserId: {}, EventId: {}.", userId, eventId);

        return service.getEventByInitiator(userId, eventId);
    }

    @ResponseBody
    @PatchMapping("/{userId}/events/{eventId}")
    public EventDto changeEvent(@PathVariable int userId,
                                @PathVariable int eventId,
                                @RequestBody @Valid EventUpdateDto eventDto) {
        log.info("Request to change event has been received. UserId: {}, EventId: {}.", userId, eventId);

        return service.changeEvent(userId, eventId, eventDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/requests")
    public EventRequestDto addRequest(@PathVariable int userId,
                                      @RequestParam int eventId) {
        log.info("Request to add new event request has been received. UserId: {}, EventId: {}.", userId, eventId);

        return service.addRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestDto cancelRequest(@PathVariable int userId,
                                         @PathVariable int requestId) {
        log.info("Request to cancel event request has been received. UserId: {}, RequestId: {}.",
                userId, requestId);

        return service.cancelRequest(userId, requestId);
    }

    @GetMapping("/{userId}/requests")
    public List<EventRequestDto> getAllUserRequests(@PathVariable int userId,
                                                    @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                                    @RequestParam(value = "size", defaultValue = "10") @Min(1) int size) {
        log.info("Request to get information about the user's event requests has been received. " +
                        "UserId: {}. From: {}, Size: {}", userId, from, size);

        return service.getAllUserRequests(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<EventRequestDto> getUserRequestsByEvent(@PathVariable int userId,
                                                        @PathVariable int eventId) {
        log.info("Request to get information about the user's event requests by specific event has been received. " +
                "UserId: {}. EventId: {}", userId, eventId);

        return service.getUserRequestsByEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public RequestStatusUpdateResult changeRequestStatus(@PathVariable int userId,
                                                         @PathVariable int eventId,
                                                         @RequestBody RequestStatusUpdateRequest request) {
        log.info("Request to change the status of event request has been received. " +
                "UserId: {}, EventId: {}, Request: {}", userId, eventId, request.toString());
        return service.changeRequestStatus(userId, eventId, request);
    }
}