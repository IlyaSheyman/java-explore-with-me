package ru.practicum.model;

import org.springframework.stereotype.Service;
import ru.practicum.dto.StatisticsDto;
import ru.practicum.dto.StatisticsForListDto;

@Service
public class StatisticsMapper {

    public StatisticsDto toStatisticsDto(Statistics stat) {
        return null;
    }

    public StatisticsForListDto toStatForListDto(ru.practicum.model.Statistics stat) {
        return StatisticsForListDto
                .builder()
                .app(stat.getApp())
                .hits(stat.getHits())
                .uri(stat.getUri())
                .build();
    }

    public ru.practicum.model.Statistics fromStatisticsDto(StatisticsDto statsDto) {
        return ru.practicum.model.Statistics
                .builder()
                .app(statsDto.getApp())
                .uri(statsDto.getUri())
                .timestamp(statsDto.getTimestamp())
                .ip(statsDto.getIp())
                .build();
    }
}
