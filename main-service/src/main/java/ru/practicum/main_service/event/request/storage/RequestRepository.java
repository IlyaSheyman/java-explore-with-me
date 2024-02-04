package ru.practicum.main_service.event.request.storage;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.event.request.model.RequestState;
import ru.practicum.main_service.event.request.model.EventRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<EventRequest, Integer> {
    List<EventRequest> findByRequester_Id(int userId, PageRequest pageRequest);

    List<EventRequest> getByEvent_Id(int eventId);

    int countByEvent_IdAndStatus(int eventId, RequestState status);

    boolean existsByRequester_IdAndEvent_Id(int userId, int eventId);
}
