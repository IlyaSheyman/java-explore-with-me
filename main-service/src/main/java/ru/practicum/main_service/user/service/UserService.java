package ru.practicum.main_service.user.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.category.model_and_dto.Category;
import ru.practicum.main_service.event.dto.EventCreateDto;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.event.dto.EventSmallDto;
import ru.practicum.main_service.event.dto.EventUpdateDto;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.request.dto.EventRequestDto;
import ru.practicum.main_service.event.request.dto.RequestStatusUpdateRequest;
import ru.practicum.main_service.event.request.dto.RequestStatusUpdateResult;
import ru.practicum.main_service.location.model.Location;

import java.util.List;

public interface UserService {
    @Transactional
    EventDto createEvent(int userId, EventCreateDto eventDto);

    Location getLocation(Location location);

    Category getCategoryById(int id);

    List<EventSmallDto> getEventsByUser(int userId, int from, int size);

    EventDto getEventByInitiator(int userId, int eventId);

    @Transactional
    EventDto changeEvent(int userId, int eventId, EventUpdateDto eventDto);

    Event updateEvent(Event event, EventUpdateDto dto);

    List<EventRequestDto> getAllUserRequests(int userId, int from, int size);

    @Transactional
    EventRequestDto addRequest(int userId, int eventId);

    List<EventRequestDto> getUserRequestsByEvent(int userId, int eventId);

    @Transactional
    EventRequestDto cancelRequest(int userId, int requestId);

    @Transactional
    RequestStatusUpdateResult changeRequestStatus(int userId, int eventId, RequestStatusUpdateRequest request);
}