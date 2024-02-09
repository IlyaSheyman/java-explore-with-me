package ru.practicum.main_service.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_service.category.model_and_dto.CategoryDto;
import ru.practicum.main_service.event.model.EventState;
import ru.practicum.main_service.user.model_and_dto.UserDto;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventSmallDto {
    private int id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private UserDto initiator;
    private boolean paid;
    private LocalDateTime eventDate;
    private int views;
    private int confirmedRequests;
    private EventState state;

}
