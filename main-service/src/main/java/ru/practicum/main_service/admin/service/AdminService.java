package ru.practicum.main_service.admin.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.category.model_and_dto.Category;
import ru.practicum.main_service.category.model_and_dto.CategoryDto;
import ru.practicum.main_service.compilation.dto.CompilationBigDto;
import ru.practicum.main_service.compilation.dto.NewCompilationDto;
import ru.practicum.main_service.compilation.dto.UpdateCompilationDto;
import ru.practicum.main_service.compilation.model.Compilation;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.event.dto.EventUpdateAdminDto;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.user.model_and_dto.User;
import ru.practicum.main_service.user.model_and_dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface AdminService {
    @Transactional
    Category addCategory(CategoryDto categoryDto);

    @Transactional
    void deleteCategory(int catId);

    @Transactional
    Category editCategory(int catId, CategoryDto catDto);

    List<User> getUsers(List<Integer> ids, int from, int size);

    @Transactional
    User addUser(UserDto userDto);

    @Transactional
    void deleteUser(int id);

    List<EventDto> getEvents(ArrayList<Integer> usersIds,
                             ArrayList<String> states,
                             ArrayList<Integer> categories,
                             LocalDateTime rangeStart,
                             LocalDateTime rangeEnd,
                             int from,
                             int size);

    void validateTimeRange(LocalDateTime rangeStart, LocalDateTime rangeEnd);

    @Transactional
    CompilationBigDto addCompilation(NewCompilationDto dto);

    @Transactional
    void deleteCompilation(int compId);

    @Transactional
    CompilationBigDto updateCompilation(int compId, UpdateCompilationDto dto);

    Compilation getCompilation(int compId);

    @Transactional
    EventDto eventAdministration(int eventId, EventUpdateAdminDto updateAdminDto);

    @Transactional
    void updateEvent(Event event, EventUpdateAdminDto dto);
}