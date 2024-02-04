package ru.practicum.main_service.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_service.event.dto.EventSmallDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationBigDto {
    @Positive
    @NotNull
    private int id;
    @NotNull
    private boolean pinned;
    @Size(max = 50)
    private String title;
    @NotNull
    private List<EventSmallDto> events;
}
