package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.StatisticsDto;
import ru.practicum.service.StatsService;

@Slf4j
@RestController
public class StatsController {

    private final StatsService service;

    @Autowired
    public StatsController(StatsService service) {
        this.service = service;
    }

    @ResponseBody
    @PostMapping("/hit")
    public StatisticsDto addStats(@RequestBody StatisticsDto stats) {
        log.info("Получен запрос на сохранение информации о запросе пользователя");
        return service.addStats(stats);
    }

    @GetMapping("/stats")
    public void getStats(String start, String end, String[] uris, boolean unique) {

    }
}
