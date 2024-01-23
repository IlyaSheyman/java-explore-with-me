package ru.practicum.main_service.event.dto;

import org.springframework.stereotype.Component;
import ru.practicum.main_service.category.model_and_dto.CategoryMapper;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event_request.model.EventRequest;
import ru.practicum.main_service.user.model_and_dto.UserMapper;

import java.util.ArrayList;

@Component
public class EventMapper {
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;

    public EventMapper(UserMapper userMapper, CategoryMapper categoryMapper) {
        this.userMapper = userMapper;
        this.categoryMapper = categoryMapper;
    }

    public Event fromEventDto(EventDto dto) {
        return Event.builder()
                .eventDate(dto.getEventDate())
                .id(dto.getId())
                .state(dto.getState())
                .title(dto.getTitle())
                .views(dto.getViews())
                .isPaid(dto.isPaid())
                .initiator(userMapper.fromUserDto(dto.getInitiator()))
                .description(dto.getDescription())
                .publishedOn(dto.getPublishedOn())
                .participantLimit(dto.getParticipantLimit())
                .annotation(dto.getAnnotation())
                .build();
    }

    public EventDto toEventDto(Event event) {
        return EventDto
                .builder()
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .confirmedRequests(event.getConfirmedRequests().size())
                .id(event.getId())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .isPaid(event.isPaid())
                .initiator(userMapper.toUserDto(event.getInitiator()))
                .description(event.getDescription())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .participantLimit(event.getParticipantLimit())
                .annotation(event.getAnnotation())
                .build();
    }

    public Event fromEventCreateDto(EventCreateDto dto) {
        return Event.builder()
                .confirmedRequests(new ArrayList<EventRequest>())
                .eventDate(dto.getEventDate())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .participantLimit(dto.getParticipantLimit())
                .annotation(dto.getAnnotation())
                .build();
    }
}