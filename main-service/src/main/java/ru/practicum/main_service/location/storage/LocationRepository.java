package ru.practicum.main_service.location.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.location.model.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Location getByLatAndLon(double lat, double lon);
}
