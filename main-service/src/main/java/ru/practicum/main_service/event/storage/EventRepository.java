package ru.practicum.main_service.event.storage;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.user.model_and_dto.User;

import javax.transaction.Transactional;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> getByInitiator(User initiator, PageRequest pageRequest);
    Event getByInitiatorAndId(User initiator, int id);

    @Modifying
    @Query("update Event e set e.views = :views where e.id = :id")
    @Transactional
    int updateViewsById(@Param("views") int views,
                        @Param("id") int eventId);
}
