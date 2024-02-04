package ru.practicum.main_service.event_request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_service.event_request.model.RequestState;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestStatusUpdateRequest {
    private RequestState status;
    private List<Integer> requestIds;

}
