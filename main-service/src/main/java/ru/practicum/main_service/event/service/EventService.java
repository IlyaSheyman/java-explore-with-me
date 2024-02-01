package ru.practicum.main_service.event.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.admin.service.AdminService;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.event.dto.EventMapper;
import ru.practicum.main_service.event.dto.EventSmallDto;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.EventState;
import ru.practicum.main_service.event.storage.EventRepository;
import ru.practicum.main_service.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository repository;
    private final EventMapper mapper;

    private final AdminService adminService;

    public EventService(EventRepository repository, EventMapper mapper, AdminService adminService) {
        this.repository = repository;
        this.mapper = mapper;
        this.adminService = adminService;
    }

    public EventDto getEventById(int id) {
        Event event = repository.findById(id).orElseThrow(() -> new NotFoundException("Событие с id " + " не найдено"));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Событие с id " + id + " не опубликовано");
        }

        return mapper.toEventDto(event);
    }

    public List<EventSmallDto> getAllEvents(String text,
                                            List<Integer> categories,
                                            Boolean paid,
                                            LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd,
                                            Boolean onlyAvailable,
                                            String sort,
                                            int from,
                                            int size) {

        adminService.validateTimeRange(rangeStart, rangeEnd);
        List<Event> allEvents = repository.findAll(PageRequest.of(from / size, size)).toList();

        if (!text.isBlank() || text != null) {
            allEvents.stream()
                    .filter(event -> event.getAnnotation().contains(text) || event.getDescription().contains(text))
                    .collect(Collectors.toList());
        }

        if (!categories.isEmpty() && categories != null) {
            allEvents.stream()
                    .filter(event -> categories.contains(event.getCategory().getId()))
                    .collect(Collectors.toList());
        }

        if (paid != null) {
            allEvents.stream()
                    .filter(event -> event.isPaid() == paid)
                    .collect(Collectors.toList());
        }

        if (rangeStart != null && rangeEnd != null) {
            allEvents.stream()
                    .filter(event -> event.getEventDate().isAfter(rangeStart)
                            && event.getEventDate().isBefore(rangeEnd))
                    .collect(Collectors.toList());
        }

        if (onlyAvailable != false) {
            allEvents.stream()
                    .filter(event -> event.getParticipantLimit() != event.getConfirmedRequests().size())
                    .collect(Collectors.toList());
        }

        if (sort.equals("EVENT_DATE")) {
            allEvents.stream()
                    .sorted(Comparator.comparing(Event::getEventDate))
                    .collect(Collectors.toList());
        } else if (sort.equals("VIEWS")) {
            allEvents.stream()
                    .sorted(Comparator.comparingInt(Event::getViews).reversed())
                    .collect(Collectors.toList());
        }

        List<EventSmallDto> filteredEvents = allEvents
                .stream()
                .map(mapper::toEventSmallDto)
                .collect(Collectors.toList());

        return filteredEvents;
    }


}