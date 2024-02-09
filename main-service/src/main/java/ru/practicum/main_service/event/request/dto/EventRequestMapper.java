package ru.practicum.main_service.event.request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.main_service.event.request.model.EventRequest;

@Component
public class EventRequestMapper {
    public EventRequestDto toRequestDto(EventRequest request) {
        return EventRequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
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
