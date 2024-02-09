package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.main_service.event.model.StateAdminAction;
import ru.practicum.main_service.location.dto.LocationDto;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.main_service.consts.Consts.TIME_FORMAT;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class EventUpdateAdminDto {
    private @Size(min = 3, max = 120) String title;
    private @Size(min = 20, max = 2000) String annotation;
    private @Size(min = 20, max = 7000) String description;
    private int category;
    private LocationDto location;
    private Boolean paid;
    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime eventDate;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAdminAction stateAction;
}