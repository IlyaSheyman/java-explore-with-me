package ru.practicum.dto;

import ru.practicum.model.Statistics;

public class StatisticsMapper {
    public StatisticsDto toStatisticsDto(Statistics stats) {
        return null;
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
