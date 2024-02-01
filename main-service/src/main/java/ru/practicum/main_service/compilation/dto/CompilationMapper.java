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
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned())
                .build();
    }

    public CompilationBigDto toCompilationBigDto(Compilation compilation) {
        return CompilationBigDto.builder()
                .id(compilation.getId())
                .events(compilation
                        .getEvents().stream()
                        .map(eventMapper::toEventSmallDto)
                        .collect(Collectors.toList()))
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .build();
    }
}
