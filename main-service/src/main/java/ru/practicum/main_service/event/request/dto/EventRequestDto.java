package ru.practicum.main_service.event.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_service.event.request.model.RequestState;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.practicum.main_service.consts.Consts.TIME_FORMAT;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {
    private @NotNull int id;

    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime created;

    private @NotNull int event;
    private @NotNull int requester;
    @Enumerated(EnumType.STRING)
    private @NotNull RequestState status;
}