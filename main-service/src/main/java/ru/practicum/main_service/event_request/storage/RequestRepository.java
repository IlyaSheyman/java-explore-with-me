package ru.practicum.main_service.event_request.storage;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.event_request.model.EventRequest;
import ru.practicum.main_service.event_request.model.RequestState;

import java.util.List;

public interface RequestRepository extends JpaRepository<EventRequest, Integer> {
    List<EventRequest> findByRequester_Id(int userId, PageRequest pageRequest);

    List<EventRequest> getByEvent_Id(int eventId);

    int countByEvent_IdAndStatus(int eventId, RequestState status);
}
