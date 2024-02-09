package ru.practicum.main_service.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_service.compilation.dto.CompilationBigDto;
import ru.practicum.main_service.compilation.service.CompilationService;
import ru.practicum.main_service.compilation.service.CompilationServiceImpl;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/compilations")
public class CompilationsController {

    @Qualifier("CompilationServiceImpl")
    private final CompilationService service;

    public CompilationsController(CompilationServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public List<CompilationBigDto> getAllCompilations(@RequestParam(value = "pinned", defaultValue = "false") boolean pinned,
                                                      @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                                      @RequestParam(value = "size", defaultValue = "10") @Min(1) int size) {
        log.info("Request to get compilations has been received. IsPinned: {}, From: {}, Size: {}", pinned, from, size);

        return service.getAllCompilations(pinned, from, size);
    }

    @GetMapping(path = "/{compId}")
    public CompilationBigDto getCompilation(@PathVariable int compId) {
        log.info("Request to get compilation by id has been received. CompilationId: {}", compId);

        return service.getCompilation(compId);
    }
}