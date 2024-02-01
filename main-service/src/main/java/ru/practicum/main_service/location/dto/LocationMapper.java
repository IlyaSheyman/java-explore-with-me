package ru.practicum.main_service.location.dto;

import org.springframework.stereotype.Component;
import ru.practicum.main_service.location.model.Location;

@Component
public class LocationMapper {
    public Location fromLocationDto(LocationDto dto) {
        return Location.builder()
                .lat(dto.getLat())
                .lon(dto.getLon())
                .build();
    }
}
