package ru.practicum.main_service.compilation.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.compilation.dto.CompilationBigDto;
import ru.practicum.main_service.compilation.dto.CompilationMapper;
import ru.practicum.main_service.compilation.storage.CompilationRepository;
import ru.practicum.main_service.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository repository;
    private final CompilationMapper mapper;

    public CompilationServiceImpl(CompilationRepository repository, CompilationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<CompilationBigDto> getAllCompilations(boolean pinned, int from, int size) {
        if (!pinned) {
            return repository.findAll(PageRequest.of(from / size, size))
                    .stream()
                    .map(mapper::toCompilationBigDto)
                    .collect(Collectors.toList());
        } else {
            return repository.findAllByPinned(pinned, PageRequest.of(from / size, size))
                    .stream()
                    .map(mapper::toCompilationBigDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public CompilationBigDto getCompilation(int compId) {
        return mapper.toCompilationBigDto(repository
                .findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id " + compId + " not found")));
    }
}