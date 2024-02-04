package ru.practicum.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.StatisticsDto;
import ru.practicum.dto.StatisticsForListDto;
import ru.practicum.server.exceptions.StatParametersException;
import ru.practicum.server.service.StatsService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    @ResponseBody
    @GetMapping("/stats")
    public List<StatisticsForListDto> getStats(@RequestParam(value = "start") String start,
                                               @RequestParam(value = "end") String end,
                                               @RequestParam(value = "uris", required = false) String[] uris,
                                               @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        log.info("Получен запрос на получение статистики по посещениям");

        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        try {
            startTime = LocalDateTime.parse(URLDecoder.decode(start, "UTF-8"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            endTime = LocalDateTime.parse(URLDecoder.decode(end, "UTF-8"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (UnsupportedEncodingException e) {
            throw new StatParametersException("Некорректные параметры для получения статистики по посещениям");
        }

        return service.getStats(startTime, endTime, uris, unique);
    }
}