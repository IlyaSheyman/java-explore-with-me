package ru.practicum.main_service.compilation.dto;

import org.springframework.stereotype.Component;
import ru.practicum.main_service.compilation.model.Compilation;
import ru.practicum.main_service.event.dto.EventMapper;

import java.util.stream.Collectors;

@Component
public class CompilationMapper {

    private final EventMapper eventMapper;

    public CompilationMapper(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    public Compilation fromCompilationNewDto(NewCompilationDto dto) {
        Boolean pinned = (dto.getPinned() != null) ? dto.getPinned() : false;

        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(pinned)
                .build();
    }

    public CompilationBigDto toCompilationBigDto(Compilation compilation) {
        CompilationBigDto dto = CompilationBigDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .build();
        if (compilation.getEvents() != null) {
            dto.setEvents(compilation
                    .getEvents().stream()
                    .map(eventMapper::toEventSmallDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
