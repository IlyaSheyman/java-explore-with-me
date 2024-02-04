package ru.practicum.main_service.event.request.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RequestStatusUpdateResult {

    private final List<EventRequestDto> confirmedRequests;
    private final List<EventRequestDto> rejectedRequests;

    public RequestStatusUpdateResult() {
        this.confirmedRequests = new ArrayList<>();
        this.rejectedRequests = new ArrayList<>();
    }
}