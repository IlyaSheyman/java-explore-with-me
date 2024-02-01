package ru.practicum.main_service.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.client.StatsClient;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.event.dto.EventSmallDto;
import ru.practicum.main_service.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main_service.consts.Consts.TIME_FORMAT;

@RestController
@Slf4j
@RequestMapping(path = "/events")
public class EventController {
    private final StatsClient statsClient;
    private final EventService service;

    public EventController(StatsClient statsClient, EventService service) {
        this.statsClient = statsClient;
        this.service = service;
    }

    @GetMapping
    public List<EventSmallDto> getAllEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false)
            @DateTimeFormat(fallbackPatterns = TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(fallbackPatterns = TIME_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size,
            HttpServletRequest httpServletRequest) {

        log.info("Получен запрос на получение событий с возможностью фильтрации");
        statsClient.postStats(httpServletRequest);

        return service.getAllEvents(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size);
    }

    @ResponseBody
    @GetMapping("/{id}")
    private EventDto getEventById(@PathVariable int id, HttpServletRequest request) {
        log.info("Получен запрос на получение события по идентификатору");
        statsClient.postStats(request);
        return service.getEventById(id);
    }
}