package ru.practicum.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.StatsClient;
import ru.practicum.event.dto.EventDto;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping(path = "/events")
public class EventController {
    private final StatsClient statsClient;

    @Autowired
    public EventController(StatsClient statsClient) {
        this.statsClient = statsClient;
    }

    @ResponseBody
    @GetMapping
    private EventDto getEvents(HttpServletRequest request) {
        log.info("Получен запрос на получение ивентов с возможностью фильтрации");
        statsClient.postStats(request);
        return null;
    }

    @ResponseBody
    @GetMapping("/{id}")
    private EventDto getEventById(@PathVariable int id, HttpServletRequest request) {
        log.info("Получен запрос на получение ивента по идентификатору");
        statsClient.postStats(request);
        return null;
    }
}
