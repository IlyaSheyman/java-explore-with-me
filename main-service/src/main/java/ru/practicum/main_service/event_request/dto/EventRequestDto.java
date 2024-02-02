package ru.practicum.main_service.event_request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_service.event_request.model.RequestState;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {
    private int id;
    private LocalDateTime created;
    private int event;
    private int requester;
    @Enumerated(EnumType.STRING)
    private RequestState status;
}