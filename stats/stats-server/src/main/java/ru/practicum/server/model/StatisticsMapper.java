package ru.practicum.server.model;

import org.springframework.stereotype.Component;
import ru.practicum.dto.StatisticsDto;
import ru.practicum.dto.StatisticsForListDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Statistics
                .builder()
                .app(statsDto.getApp())
                .uri(statsDto.getUri())
                .timestamp(LocalDateTime.parse(statsDto.getTimestamp(), formatter))
                .ip(statsDto.getIp())
                .build();
    }
}