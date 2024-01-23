package ru.practicum.main_service.event.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
