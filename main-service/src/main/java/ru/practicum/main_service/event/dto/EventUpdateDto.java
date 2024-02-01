package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_service.event.model.StateAction;
import ru.practicum.main_service.location.model.Location;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.main_service.consts.Consts.TIME_FORMAT;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdateDto {
    @Size(min = 3, max = 120)
    private String title;
    @Size(min = 3, max = 1500)
    private String annotation;
    @Size(min = 3, max = 6000)
    private String description;
    private int category;
    private Boolean paid;
    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime eventDate;
    @PositiveOrZero
    private int participantLimit;
    private Location location;
    private Boolean requestModeration;
    private StateAction stateAction;
}