package ru.practicum.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatisticsDto;
import ru.practicum.dto.StatisticsForListDto;
import ru.practicum.server.model.Statistics;
import ru.practicum.server.model.StatisticsMapper;
import ru.practicum.server.storage.StatisticsRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StatsService {

    private final StatisticsMapper mapper;
    private final StatisticsRepository repository;

    @Autowired
    public StatsService(StatisticsMapper mapper, StatisticsRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public StatisticsDto addStats(StatisticsDto stats) {
        Statistics stat = mapper.fromStatisticsDto(stats);

        if (repository.findByIpAndUriAndApp(stat.getIp(), stat.getUri(), stat.getApp()) != null) {
            int statId = repository.findByIpAndUriAndApp(stat.getIp(), stat.getUri(), stat.getApp()).getId();
            stat.setId(statId);
            stat.setHits(repository.getById(statId).getHits() + 1);
        } else {
            stat.setHits(1);
        }

        repository.save(stat);

        return stats;
    }

    public List<StatisticsForListDto> getStats(LocalDateTime start,
                                               LocalDateTime end,
                                               String[] uris,
                                               boolean unique) {
        List<Statistics> allStats = repository.findByTimestampBetween(start, end);
        List<Statistics> resultStats;

        if (unique) {
            resultStats = allStats.stream()
                    .filter(stat -> Arrays.asList(uris).contains(stat.getUri()))
                    .filter(stat -> allStats.stream()
                            .filter(s -> s.getUri().equals(stat.getUri()))
                            .map(Statistics::getIp)
                            .distinct()
                            .count() == 1)
                    .collect(Collectors.toList());
        } else {
            resultStats = allStats.stream()
                    .filter(stat -> Arrays.asList(uris).contains(stat.getUri()))
                    .collect(Collectors.toList());
        }

        List<StatisticsForListDto> finalStats = resultStats
                .stream()
                .map(mapper::toStatForListDto)
                .collect(Collectors.toList());

        return finalStats;
    }
}