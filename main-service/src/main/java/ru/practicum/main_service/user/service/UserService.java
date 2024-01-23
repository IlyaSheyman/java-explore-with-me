package ru.practicum.main_service.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.main_service.category.model_and_dto.Category;
import ru.practicum.main_service.category.storage.CategoryRepository;
import ru.practicum.main_service.event.dto.EventCreateDto;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.event.dto.EventMapper;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.EventState;
import ru.practicum.main_service.event.storage.EventRepository;
import ru.practicum.main_service.exception.IncorrectRequestException;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.user.model_and_dto.User;
import ru.practicum.main_service.user.storage.UserRepository;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    public UserService(UserRepository userRepository, CategoryRepository categoryRepository, EventRepository eventRepository, EventMapper eventMapper) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    public EventDto createEvent(int userId, EventCreateDto eventDto) {
        checkEventDate(eventDto.getEventDate());

        User initiator = getUserById(userId);
        Category category = getCategoryById(eventDto.getCategory());
        LocalDateTime eventDate = eventDto.getEventDate();

        Event event = eventMapper.fromEventCreateDto(eventDto);
        event.setInitiator(initiator);
        event.setEventDate(eventDate);
        event.setCategory(category);
        event.setState(EventState.PENDING);
        event.setCreatedOn(LocalDateTime.now());

        Event eventWithId = eventRepository.save(event);
        System.out.println(event.toString());
        return eventMapper.toEventDto(eventWithId);
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now())) {
            throw new IncorrectRequestException("Ивент не может быть раньше " + LocalDateTime.now());
        } else if (eventDate.isBefore(LocalDateTime.now().plusHours(3))) {
            throw new IncorrectRequestException("Ивент должен быть минимум через 3 часа");
        }
    }

    private User getUserById(int userId) {
        if (userRepository.existsById(userId)) {
            return userRepository.getById(userId);
        } else {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
    }

    private Category getCategoryById(int id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с id " + id + " не найдена"));
    }
}