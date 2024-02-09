package ru.practicum.server.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.StatisticsDto;
import ru.practicum.dto.StatisticsForListDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    @Transactional
    StatisticsDto addStats(StatisticsDto stats);

    List<StatisticsForListDto> getStats(LocalDateTime start,
                                        LocalDateTime end,
                                        String[] uris,
                                        boolean unique);
}
