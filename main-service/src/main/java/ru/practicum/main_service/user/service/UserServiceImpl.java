package ru.practicum.main_service.user.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.category.model_and_dto.Category;
import ru.practicum.main_service.category.storage.CategoryRepository;
import ru.practicum.main_service.event.dto.EventCreateDto;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.event.dto.EventMapper;
import ru.practicum.main_service.event.dto.EventSmallDto;
import ru.practicum.main_service.event.dto.EventUpdateDto;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.EventState;
import ru.practicum.main_service.event.model.StateAction;
import ru.practicum.main_service.event.request.dto.EventRequestDto;
import ru.practicum.main_service.event.request.dto.EventRequestMapper;
import ru.practicum.main_service.event.request.dto.RequestStatusUpdateRequest;
import ru.practicum.main_service.event.request.dto.RequestStatusUpdateResult;
import ru.practicum.main_service.event.request.model.EventRequest;
import ru.practicum.main_service.event.request.model.RequestState;
import ru.practicum.main_service.event.request.storage.RequestRepository;
import ru.practicum.main_service.event.storage.EventRepository;
import ru.practicum.main_service.exception.ConflictRequestException;
import ru.practicum.main_service.exception.IncorrectRequestException;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.location.model.Location;
import ru.practicum.main_service.location.storage.LocationRepository;
import ru.practicum.main_service.user.model_and_dto.User;
import ru.practicum.main_service.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;

    private final EventMapper eventMapper;
    private final EventRequestMapper requestMapper;

    public UserServiceImpl(UserRepository userRepository,
                           CategoryRepository categoryRepository,
                           EventRepository eventRepository,
                           LocationRepository locationRepository,
                           RequestRepository requestRepository, EventMapper eventMapper, EventRequestMapper requestMapper) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.requestRepository = requestRepository;
        this.eventMapper = eventMapper;
        this.requestMapper = requestMapper;
    }

    @Override
    @Transactional
    public EventDto createEvent(int userId, EventCreateDto eventDto) {
        checkEventDate(eventDto.getEventDate());

        User initiator = getUserById(userId);
        Category category = getCategoryById(eventDto.getCategory());
        LocalDateTime eventDate = eventDto.getEventDate();
        Location location = getLocation(eventDto.getLocation());

        Event event = eventMapper.fromEventCreateDto(eventDto);
        event.setInitiator(initiator);
        event.setEventDate(eventDate);
        event.setCategory(category);
        event.setState(EventState.PENDING);

        int limit = eventDto.getParticipantLimit();
        event.setParticipantLimit(limit);

        if (eventDto.getPaid() == null) {
            eventDto.setPaid(false);
        } else {
            event.setPaid(eventDto.getPaid());
        }

        Boolean requestModeration = eventDto.getRequestModeration();
        event.setRequestModeration(requestModeration == null || requestModeration);

        event.setCreatedOn(LocalDateTime.now());
        event.setLocation(location);

        Event eventWithId = eventRepository.save(event);
        System.out.println(event.toString());
        return eventMapper.toEventDto(eventWithId);
    }

    @Override
    public Location getLocation(Location location) {
        if (location != null) {
            double lat = location.getLat();
            double lon = location.getLon();
            Location loc = locationRepository.getByLatAndLon(lat, lon);
            if (loc != null) {
                return loc;
            } else {
                return locationRepository.save(location);
            }
        } else {
            return null;
        }
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now())) {
            throw new IncorrectRequestException("Event can not be earlier then " + LocalDateTime.now());
        } else if (eventDate.isBefore(LocalDateTime.now().plusHours(3))) {
            throw new IncorrectRequestException("The event must be in at least 3 hours");
        }
    }

    private User getUserById(int userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found"));
    }

    @Override
    public List<EventSmallDto> getEventsByUser(int userId, int from, int size) {
        List<Event> events = eventRepository
                .getByInitiator(
                        getUserById(userId),
                        PageRequest.of(from / size, size));

        if (events == null) {
            return Collections.emptyList();
        }
        List<EventSmallDto> eventsDto = events.stream()
                .map(eventMapper::toEventSmallDto)
                .collect(Collectors.toList());
        return eventsDto;
    }

    @Override
    public EventDto getEventByInitiator(int userId, int eventId) {
        User initiator = getUserById(userId);
        Event event = eventRepository.getByInitiatorAndId(initiator, eventId);

        if (event == null) {
            throw new NotFoundException("Event with userId " + userId + " not found");
        }

        return eventMapper.toEventDto(event);
    }

    @Override
    @Transactional
    public EventDto changeEvent(int userId, int eventId, EventUpdateDto eventDto) {
        LocalDateTime newEventTime = eventDto.getEventDate();
        if (newEventTime != null) {
            checkEventDate(newEventTime);
        }

        User initiator = getUserById(userId);
        Event event = eventRepository.getByInitiatorAndId(initiator, eventId);

        if (event == null) {
            throw new NotFoundException("Event with userId " + userId + " not found");
        }

        EventState state = event.getState();
        if (state.equals(EventState.PUBLISHED)) {
            throw new ConflictRequestException("Only events with pending moderation can be changed");
        }
        Event updated = updateEvent(event, eventDto);

        StateAction stateAction = eventDto.getStateAction();

        if (stateAction != null) {
            if (stateAction.equals(StateAction.SEND_TO_REVIEW)) {
                updated.setState(EventState.PENDING);
            } else if (stateAction.equals(StateAction.CANCEL_REVIEW)) {
                updated.setState(EventState.CANCELED);
            }
        }

        return eventMapper.toEventDto(eventRepository.save(updated));
    }

    @Override
    public Event updateEvent(Event event, EventUpdateDto dto) {
        if (dto.getTitle() != null) event.setTitle(dto.getTitle());
        if (dto.getEventDate() != null) event.setEventDate(dto.getEventDate());
        if (dto.getAnnotation() != null) event.setAnnotation(dto.getAnnotation());
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getCategory() != 0) event.setCategory(getCategoryById(dto.getCategory()));
        if (dto.getLocation() != null) event.setLocation(getLocation(dto.getLocation()));
        if (dto.getPaid() != null) event.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != 0) event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());
        return event;
    }

    @Override
    public List<EventRequestDto> getAllUserRequests(int userId, int from, int size) {
        User user = getUserById(userId);
        List<EventRequest> requests = requestRepository
                .findByRequester_Id(userId, PageRequest.of(from / size, size));

        if (requests == null) {
            requests = new ArrayList<>();
        }

        return requests
                .stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestDto addRequest(int userId, int eventId) {
        User requester = getUserById(userId);
        Event event = getEventById(eventId);

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictRequestException("You cannot participate in an unpublished event");
        }
        if (userId == event.getInitiator().getId()) {
            throw new ConflictRequestException("The event organizer cannot send a request to participate");
        }
        if (requestRepository.existsByRequester_IdAndEvent_Id(userId, eventId)) {
            throw new ConflictRequestException("This user's request to participate in this event already exists");
        }


        EventRequest request = EventRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .status(getRequestState(event))
                .build();

        EventRequest result = requestRepository.save(request);

        return requestMapper.toRequestDto(result);
    }

    private RequestState getRequestState(Event event) {
        int confirmed = event.getConfirmedRequests();

        if (event.getParticipantLimit() == 0) {
            event.setConfirmedRequests(++confirmed);
            eventRepository.save(event);
            return RequestState.CONFIRMED;
        }

        if (!event.isRequestModeration()) {
            if (confirmed == event.getParticipantLimit()) {
                throw new ConflictRequestException("The participant limit has been reached");
            } else {
                event.setConfirmedRequests(++confirmed);
                eventRepository.save(event);
                return RequestState.CONFIRMED;
            }
        }
        return RequestState.PENDING;
    }

    private Event getEventById(int eventId) {
        return eventRepository
                .findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found"));
    }

    @Override
    public List<EventRequestDto> getUserRequestsByEvent(int userId, int eventId) {
        Event event = getEventById(eventId);
        User user = getUserById(userId);

        if (event.getInitiator().getId() != user.getId()) {
            throw new IncorrectRequestException("User with id " + userId
                    + " is not the initiator of event with id " + eventId);
        }

        List<EventRequest> requests = requestRepository.getByEvent_Id(eventId);

        return Optional.ofNullable(requests)
                .map(list -> list.stream()
                        .map(requestMapper::toRequestDto)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Override
    @Transactional
    public EventRequestDto cancelRequest(int userId, int requestId) {
        EventRequest request = getRequestById(requestId);
        User requester = getUserById(userId);

        if (!request.getRequester().equals(requester)) {
            throw new IncorrectRequestException("User's id must be the same as the request author's id");
        }
        request.setStatus(RequestState.CANCELED);

        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    private EventRequest getRequestById(int requestId) {
        return requestRepository
                .findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id " + requestId + " not found"));
    }

    @Override
    @Transactional
    public RequestStatusUpdateResult changeRequestStatus(int userId, int eventId, RequestStatusUpdateRequest request) {
        getUserById(userId);
        Event event = getEventById(eventId);
        RequestState newStatus = request.getStatus();

        checkConfirmation(event);
        List<Integer> requestIds = request.getRequestIds();
        List<EventRequest> requests = requestRepository.findAllById(requestIds);

        if (requests == null) {
            throw new NotFoundException("Requests not found");
        } else if (requests.size() != requestIds.size()) {
            throw new IncorrectRequestException("The list of identifiers contains those for which requests were not found");
        }

        int participantLimit = event.getParticipantLimit();
        int requestsNumber = requestRepository.countByEvent_IdAndStatus(eventId, RequestState.CONFIRMED);
        RequestStatusUpdateResult result = new RequestStatusUpdateResult();

        checkRequestsLimit(requestsNumber, participantLimit);

        for (EventRequest req : requests) {
            checkStatus(req.getStatus());

            if (newStatus.equals(RequestState.CONFIRMED)) {
                req.setStatus(RequestState.CONFIRMED);
                requestsNumber++;
                result.getConfirmedRequests().add(requestMapper.toRequestDto(req));
            } else {
                req.setStatus(RequestState.REJECTED);
                result.getRejectedRequests().add(requestMapper.toRequestDto(req));
            }
        }

        requestRepository.saveAll(requests);
        return result;
    }

    private void checkStatus(RequestState status) {
        if (!status.equals(RequestState.PENDING)) {
            throw new ConflictRequestException("To perform an action the application status must be PENDING");
        }
    }

    private void checkRequestsLimit(int requestsNumber, int participantLimit) {
        if (participantLimit <= requestsNumber) {
            throw new ConflictRequestException("The limit on the number of participants has been reached," +
                    " so requests cannot be confirmed");
        }
    }

    private void checkConfirmation(Event event) {
        if (event.getParticipantLimit() == 0 && !event.isRequestModeration()) {
            throw new ConflictRequestException("Request confirmation is not needed");
        }
    }
}