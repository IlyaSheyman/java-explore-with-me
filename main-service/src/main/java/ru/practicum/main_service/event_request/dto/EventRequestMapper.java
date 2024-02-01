package ru.practicum.main_service.event_request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.main_service.event_request.model.EventRequest;

@Component
public class EventRequestMapper {
    public EventRequestDto toRequestDto(EventRequest request) {
        return EventRequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }

    public EventRequest fromRequestDto(EventRequestDto dto) {
        return EventRequest.builder()
                .status(dto.getStatus())
                .created(dto.getCreated())
                .build();
    }
}
