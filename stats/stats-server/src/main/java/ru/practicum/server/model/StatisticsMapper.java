package ru.practicum.server.model;

import org.springframework.stereotype.Component;
import ru.practicum.dto.StatisticsDto;
import ru.practicum.dto.StatisticsForListDto;

@Component
public class StatisticsMapper {

    public StatisticsDto toStatisticsDto(Statistics stat) {
        return null;
    }

    public StatisticsForListDto toStatForListDto(Statistics stat) {
        return StatisticsForListDto
                .builder()
                .app(stat.getApp())
                .hits(stat.getHits())
                .uri(stat.getUri())
                .build();
    }

    public Statistics fromStatisticsDto(StatisticsDto statsDto) {
        return Statistics
                .builder()
                .app(statsDto.getApp())
                .uri(statsDto.getUri())
                .timestamp(statsDto.getTimestamp())
                .ip(statsDto.getIp())
                .build();
    }
}
