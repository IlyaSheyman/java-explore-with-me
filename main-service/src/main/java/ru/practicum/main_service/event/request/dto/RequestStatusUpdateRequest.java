package ru.practicum.main_service.event.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.main_service.event.request.model.RequestState;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RequestStatusUpdateRequest {
    private RequestState status;
    private List<Integer> requestIds;

}