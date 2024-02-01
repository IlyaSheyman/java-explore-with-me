package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_service.event.model.StateAction;
import ru.practicum.main_service.event.model.StateAdminAction;
import ru.practicum.main_service.location.dto.LocationDto;

import java.time.LocalDateTime;

import static ru.practicum.main_service.consts.Consts.TIME_FORMAT;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventUpdateAdminDto {
    private String title;
    private String annotation;
    private String description;
    private int category;
    private LocationDto location;
    private Boolean paid;
    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime eventDate;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAdminAction stateAction;
}
