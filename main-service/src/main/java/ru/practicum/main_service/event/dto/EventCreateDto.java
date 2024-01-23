package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @NotNull
    @Size(min = 3, max = 120)
    private String title;
    @NotNull
    @Size(min = 3, max = 1500)
    private String annotation;

    @NotBlank
    @Size(min = 3, max = 6000)
    private String description;

    @NotNull
    @Positive
    private int category;

    @NotNull
    private Boolean paid;

    @JsonFormat(pattern = TIME_FORMAT)
    @Future
    private LocalDateTime eventDate;

    @PositiveOrZero
    private int participantLimit;

    @NotNull
    private Boolean requestModeration;
}