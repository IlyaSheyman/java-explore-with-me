package ru.practicum.dto;

import lombok.Data;

@Data
public class StatisticsListDto {
    private String app;
    private String uri;
    private int hits;
}
