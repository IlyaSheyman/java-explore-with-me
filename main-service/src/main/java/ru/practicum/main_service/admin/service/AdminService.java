package ru.practicum.main_service.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.category.model_and_dto.Category;
import ru.practicum.main_service.category.model_and_dto.CategoryDto;
import ru.practicum.main_service.category.model_and_dto.CategoryMapper;
import ru.practicum.main_service.category.storage.CategoryRepository;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.event.dto.EventMapper;
import ru.practicum.main_service.event.storage.EventRepository;
import ru.practicum.main_service.exception.IncorrectRequestException;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.user.model_and_dto.User;
import ru.practicum.main_service.user.model_and_dto.UserDto;
import ru.practicum.main_service.user.model_and_dto.UserMapper;
import ru.practicum.main_service.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AdminService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Autowired
    public AdminService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, UserRepository userRepository, UserMapper userMapper, EventRepository eventRepository, EventMapper eventMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    public Category addCategory(CategoryDto categoryDto) {
        if (categoryRepository.findAllNames().contains(categoryDto.getName())) {
            throw new IncorrectRequestException("Некорректный запрос: имя категории должно быть уникальным");
        } else {
            Category category = categoryMapper.fromCategoryDto(categoryDto);
            categoryRepository.save(category);
            return category;
        }
    }

    public void deleteCategory(int catId) {
        if (categoryRepository.findById(catId) != null) {
            categoryRepository.deleteById(catId);
        } else {
            throw new NotFoundException("Категория с id " + catId + " не найдена");
        }
        //TODO добавить проверку на то, что с категорией не связан ни один ивент
    }

    public Category editCategory(int catId, CategoryDto catDto) {
        if (categoryRepository.findById(catId) != null) {
            if (categoryRepository.findAllNames().contains(catDto.getName())) {
                throw new IncorrectRequestException("Некорректный запрос: имя категории должно быть уникальным");
            } else {
                Category category = categoryMapper.fromCategoryDto(catDto);
                category.setId(catId);
                categoryRepository.save(category);
                return category;
            }
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

    public User addUser(UserDto userDto) {
        String email = userDto.getEmail();
        if (userRepository.findByEmail(email) != null) {
            throw new IncorrectRequestException("Пользователь с email " + email + " уже существует");
        } else {
            return userRepository.save(userMapper.fromUserDto(userDto));
        }
    }

    public void deleteUser(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id); //TODO проверить, что в бд есть констрейнт на удаление связанных сущностей
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
        validateTimeRange(rangeStart, rangeEnd);
        return null;
    }

    private void validateTimeRange(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeEnd == null || rangeStart == null) {
            throw new IncorrectRequestException("Один из временных параметров не указан");
        } else if (rangeEnd.isBefore(rangeStart)) {
            throw new IncorrectRequestException("Конец временного промежутка раньше начала");
        } else if (rangeEnd.equals(rangeStart)) {
            throw new IncorrectRequestException("Конец и начало совпадают");
        }
    }
}