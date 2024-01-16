package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatisticsDto;
import ru.practicum.dto.StatisticsMapper;
import ru.practicum.model.Statistics;
import ru.practicum.storage.StatisticsRepository;

@Slf4j
@Service
public class StatsService {

    private final StatisticsMapper mapper;
    private final StatisticsRepository repository;

    public StatsService(StatisticsMapper mapper, StatisticsRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public StatisticsDto addStats(StatisticsDto stats) {
        Statistics stat = mapper.fromStatisticsDto(stats);
        int statId = repository.findByIpAndUriAndApp(stat.getIp(), stat.getUri(), stat.getApp()).getId();
        stat.setId(statId);

        if (repository.existsById(statId)) {
            stat.setHits(repository.getById(statId).getHits() + 1);
        } else {
            stat.setHits(1);
        }

        repository.save(stat);

        return stats;
    }
}
