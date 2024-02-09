package ru.practicum.main_service.compilation.service;

import ru.practicum.main_service.compilation.dto.CompilationBigDto;

import java.util.List;

public interface CompilationService {
    List<CompilationBigDto> getAllCompilations(boolean pinned, int from, int size);

    CompilationBigDto getCompilation(int compId);
}
