package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_service.category.model_and_dto.CategoryDto;
import ru.practicum.main_service.event.model.EventState;
import ru.practicum.main_service.user.model_and_dto.UserDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

import static ru.practicum.main_service.consts.Consts.TIME_FORMAT;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    @NotNull
    private int id;
    @NotNull
    private String title;
    @NotNull
    private String annotation;
    @NotNull
    private String description;
    @NotNull
    private CategoryDto category;
    @NotNull
    private UserDto initiator;
    @NotNull
    private boolean isPaid;
    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime createdOn;
    @NotNull
    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime publishedOn;
    @NotNull
    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime eventDate;
    @Min(0)
    private int confirmedRequests;
    @PositiveOrZero
    private int participantLimit;
    @NotNull
    private boolean requestModeration;
    private EventState state;
    @Min(0)
    private int views;
}