package ru.practicum.main_service.event.storage;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.user.model_and_dto.User;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> getByInitiator(User initiator, PageRequest pageRequest);
    Event getByInitiatorAndId(User initiator, int id);
}
