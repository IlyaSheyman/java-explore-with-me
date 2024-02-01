package ru.practicum.main_service.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_service.compilation.dto.CompilationBigDto;
import ru.practicum.main_service.compilation.service.CompilationService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/compilations")
public class CompilationsController {

    private final CompilationService service;

    public CompilationsController(CompilationService service) {
        this.service = service;
    }

    @GetMapping
    public List<CompilationBigDto> getAllCompilations(@RequestParam(value = "pinned", defaultValue = "false") boolean pinned,
                                                   @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                                   @RequestParam(value = "size", defaultValue = "10") @Min(1) int size) {
        log.info("Получен запрос на получение подборок событий");
        return service.getAllCompilations(pinned, from, size);
    }

    @GetMapping(path = "/{compId}")
    public CompilationBigDto getCompilation(@PathVariable int compId) {
        log.info("Получен запрос на получение подборки с id " + compId);
        return service.getCompilation(compId);
    }
}
