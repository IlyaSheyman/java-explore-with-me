package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_service.location.model.Location;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.main_service.consts.Consts.TIME_FORMAT;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateDto {
    @NotBlank
    private @Size(min = 3, max = 120) String title;

    @NotBlank
    private @Size(min = 20, max = 2000) String annotation;

    @NotBlank
    private @Size(min = 20, max = 7000) String description;

    @NotNull
    @Positive
    private int category;
    private Boolean paid;

    @JsonFormat(pattern = TIME_FORMAT)
    @Future
    private LocalDateTime eventDate;

    @PositiveOrZero
    private int participantLimit;

    @NotNull
    private Location location;
    private Boolean requestModeration;
}