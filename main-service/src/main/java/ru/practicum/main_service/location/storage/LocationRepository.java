package ru.practicum.main_service.location.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.location.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> getByLatAndLon(double lat, double lon);

    Location findByLatAndLon(Double lat, Double lon);
}
