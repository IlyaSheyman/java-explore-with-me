package ru.practicum.main_service.admin.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.category.model_and_dto.Category;
import ru.practicum.main_service.category.model_and_dto.CategoryDto;
import ru.practicum.main_service.category.model_and_dto.CategoryMapper;
import ru.practicum.main_service.category.storage.CategoryRepository;
import ru.practicum.main_service.compilation.dto.CompilationBigDto;
import ru.practicum.main_service.compilation.dto.CompilationMapper;
import ru.practicum.main_service.compilation.dto.NewCompilationDto;
import ru.practicum.main_service.compilation.dto.UpdateCompilationDto;
import ru.practicum.main_service.compilation.model.Compilation;
import ru.practicum.main_service.compilation.storage.CompilationRepository;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.event.dto.EventMapper;
import ru.practicum.main_service.event.dto.EventUpdateAdminDto;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.EventState;
import ru.practicum.main_service.event.model.StateAdminAction;
import ru.practicum.main_service.event.storage.EventRepository;
import ru.practicum.main_service.exception.ConflictRequestException;
import ru.practicum.main_service.exception.IncorrectRequestException;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.location.dto.LocationDto;
import ru.practicum.main_service.location.dto.LocationMapper;
import ru.practicum.main_service.location.model.Location;
import ru.practicum.main_service.location.storage.LocationRepository;
import ru.practicum.main_service.user.model_and_dto.User;
import ru.practicum.main_service.user.model_and_dto.UserDto;
import ru.practicum.main_service.user.model_and_dto.UserMapper;
import ru.practicum.main_service.user.service.UserService;
import ru.practicum.main_service.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class AdminService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserService userService;

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Transactional
    public Category addCategory(CategoryDto categoryDto) {
        if (categoryRepository.findAllNames().contains(categoryDto.getName())) {
            throw new ConflictRequestException("Некорректный запрос: имя категории должно быть уникальным");
        } else {
            Category category = categoryMapper.fromCategoryDto(categoryDto);
            categoryRepository.save(category);
            return category;
        }
    }

    @Transactional
    public void deleteCategory(int catId) {
        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictRequestException("Нельзя удалить категорию, к которой привязаны события");
        } else if (categoryRepository.findById(catId) != null) {
            categoryRepository.deleteById(catId);
        } else {
            throw new NotFoundException("Категория с id " + catId + " не найдена");
        }
    }

    @Transactional
    public Category editCategory(int catId, CategoryDto catDto) {
        if (categoryRepository.findById(catId) != null) {
            Category previous = categoryRepository.getById(catId);

            Category updated = categoryMapper.fromCategoryDto(catDto);
            updated.setId(catId);

            if (!previous.getName().equals(updated.getName())) {
                if (categoryRepository.existsByNameIgnoreCase(catDto.getName())) {
                    throw new ConflictRequestException("Категория с таким именем уже существует.");
                } else {
                    updated.setName(catDto.getName());
                }
            }

            categoryRepository.save(updated);
            return updated;
        } else {
            throw new NotFoundException("Категория с id " + catId + " не найдена");
        }
    }

    public List<User> getUsers(List<Integer> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Page<User> users;

        if (ids != null && !ids.isEmpty()) {
            users = userRepository.findByIdIn(ids, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        return users.getContent();
    }

    @Transactional
    public User addUser(UserDto userDto) {
        String email = userDto.getEmail();
        if (userRepository.findByEmail(email) != null) {
            throw new ConflictRequestException("Пользователь с email " + email + " уже существует");
        } else {
            return userRepository.save(userMapper.fromUserDto(userDto));
        }
    }

    @Transactional
    public void deleteUser(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id); // TODO проверить, что в бд есть констрейнт на удаление связанных сущностей
        } else {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
    }

    public List<EventDto> getEvents(ArrayList<Integer> usersIds,
                                    ArrayList<String> states,
                                    ArrayList<Integer> categories,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd,
                                    int from,
                                    int size) {


        List<Event> allEvents = eventRepository.findAll(PageRequest.of(from / size, size)).toList();

        if (rangeStart != null && rangeEnd != null) {
            validateTimeRange(rangeStart, rangeEnd);
            allEvents.stream()
                    .filter(event -> event.getEventDate().isAfter(rangeStart) && event.getEventDate().isBefore(rangeEnd))
                    .collect(Collectors.toList());
        }

        if (usersIds != null && !usersIds.isEmpty()) {
            allEvents.stream()
                    .filter(event -> usersIds.contains(event.getInitiator().getId()))
                    .collect(Collectors.toList());
        }

        if (states != null && !states.isEmpty()) {
            allEvents.stream()
                    .filter(event -> states.contains(event.getState()))
                    .collect(Collectors.toList());
        }

        if (categories != null && !categories.isEmpty()) {
            allEvents.stream()
                    .filter(event -> categories.contains(event.getCategory()))
                    .collect(Collectors.toList());
        }

        List<EventDto> filteredEvents = allEvents.stream().map(eventMapper::toEventDto).collect(Collectors.toList());

        return filteredEvents;
    }

    public void validateTimeRange(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
       if (rangeEnd.isBefore(rangeStart)) {
            throw new IncorrectRequestException("Конец временного промежутка раньше начала");
        } else if (rangeEnd.equals(rangeStart)) {
            throw new IncorrectRequestException("Конец и начало совпадают");
        }
    }

    @Transactional
    public CompilationBigDto addCompilation(NewCompilationDto dto) {
        checkTitle(dto.getTitle());
        Compilation compilation = compilationMapper.fromCompilationNewDto(dto);

        if (dto.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllById(dto.getEvents()));
        }

        compilationRepository.save(compilation);

        CompilationBigDto resultDto = compilationMapper.toCompilationBigDto(compilation);
        return resultDto;
    }

    private void checkTitle(String title) {
        if (compilationRepository.existsByTitle(title)) {
            throw new ConflictRequestException("Компиляция с названием " + title + " уже существует");
        }
    }

    @Transactional
    public void deleteCompilation(int compId) {
        if (compilationRepository.existsById(compId)) {
            compilationRepository.deleteById(compId);
        } else {
            throw new NotFoundException("Подборка с id " + compId + " не найдена");
        }
    }

    @Transactional
    public CompilationBigDto updateCompilation(int compId, UpdateCompilationDto dto) {
        checkTitle(dto.getTitle());

        Compilation compilation = getCompilation(compId);
        compilation.setTitle(dto.getTitle());

        if (dto.getPinned() != null) {
            compilation.setPinned(dto.getPinned());
        }

        List<Integer> eventIds = dto.getEvents();

        if (dto.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllById(eventIds));
        }

        return compilationMapper.toCompilationBigDto(compilationRepository.save(compilation));
    }

    private Compilation getCompilation(int compId) {
        return compilationRepository
                .findById(compId)
                .orElseThrow(() -> new NotFoundException("Компиляция с id " + compId + " уже существует"));
    }


    @Transactional
    public EventDto eventAdministration(int eventId, EventUpdateAdminDto updateAdminDto) {
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id " + eventId + " не найдено"));

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictRequestException("Событие уже опубликовано");
        } else if (event.getState().equals(EventState.CANCELED)) {
            throw new ConflictRequestException("Событие отменено, его невозможно опубликовать");
        }

        checkAdminTime(event.getEventDate());
        checkAdminTime(updateAdminDto.getEventDate());
        changeState(event, updateAdminDto.getStateAction());
        updateEvent(event, updateAdminDto);

        eventRepository.save(event);
        return eventMapper.toEventDto(event);
    }

    @Transactional
    private void updateEvent(Event event, EventUpdateAdminDto dto) {
        LocationDto locDto = dto.getLocation();

        if (dto.getTitle() != null)
            event.setTitle(dto.getTitle());

        if (dto.getEventDate() != null)
            event.setEventDate(dto.getEventDate());

        if (dto.getAnnotation() != null)
            event.setAnnotation(dto.getAnnotation());

        if (dto.getDescription() != null)
            event.setDescription(dto.getDescription());

        if (dto.getCategory() != 0)
            event.setCategory(userService.getCategoryById(dto.getCategory()));

        if (dto.getLocation() != null) {
            Location location = locationRepository.getByLatAndLon(locDto.getLat(), locDto.getLon());

            if (location != null) {
                event.setLocation(location);
            } else {
                event.setLocation(locationRepository.save(locationMapper.fromLocationDto(locDto)));
            }
        }

        if (dto.getPaid() != null)
            event.setPaid(dto.getPaid());

        if (dto.getParticipantLimit() != null)
            event.setParticipantLimit(dto.getParticipantLimit());

        if (dto.getRequestModeration() != null)
            event.setRequestModeration(dto.getRequestModeration());
    }

    private void changeState(Event event, StateAdminAction stateAction) {
        switch (stateAction) {
            case REJECT_EVENT:
                event.setState(EventState.CANCELED);
                break;
            case PUBLISH_EVENT:
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                break;
            default:
                throw new IncorrectRequestException("Указано некорректное действие по администрации события");
        }
    }

    private void checkAdminTime(LocalDateTime eventDate) {
        if (eventDate != null) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime limit = now.minusMinutes(60);
            if (eventDate.isBefore(limit)) {
                throw new IncorrectRequestException("Дата начала изменяемого события должна быть не ранее чем за час " +
                        "от даты публикации");
            }
        }
    }
}