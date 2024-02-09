package ru.practicum.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.StatisticsDto;
import ru.practicum.dto.StatisticsForListDto;
import ru.practicum.server.model.Statistics;
import ru.practicum.server.model.StatisticsMapper;
import ru.practicum.server.storage.StatisticsRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StatsServiceImpl implements StatsService {

    private final StatisticsMapper mapper;
    private final StatisticsRepository repository;

    @Autowired
    public StatsServiceImpl(StatisticsMapper mapper, StatisticsRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    @Transactional
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

    @Override
    public List<StatisticsForListDto> getStats(LocalDateTime start,
                                               LocalDateTime end,
                                               String[] uris,
                                               boolean unique) {

        List<Statistics> allStats = repository.findByTimestampBetween(start, end);
        List<StatisticsForListDto> resultStats;

        if (unique) {
            if (uris == null || uris.length == 0) {
                resultStats = repository.getByCreatedBetweenAndUniqueIp(start, end);
            } else {
                resultStats = repository.getByCreatedBetweenAndUriInAndUniqueIp(start, end, uris);
            }
        } else {
            if (uris == null || uris.length == 0) {
                resultStats = repository
                        .findByTimestampBetween(start, end)
                        .stream()
                        .map(mapper::toStatForListDto)
                        .collect(Collectors.toList());
            } else {
                resultStats = allStats.stream()
                        .filter(stat -> Arrays.asList(uris).contains(stat.getUri()))
                        .map(mapper::toStatForListDto)
                        .collect(Collectors.toList());
            }
        }

        List<StatisticsForListDto> finalStats = resultStats
                .stream()
                .sorted(Comparator.comparingInt(StatisticsForListDto::getHits).reversed())
                .collect(Collectors.toList());

        return finalStats;
    }
}