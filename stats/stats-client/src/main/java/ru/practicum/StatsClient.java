package ru.practicum;

import org.springframework.web.client.RestTemplate;

public class StatsClient {
    protected final RestTemplate rest;

    public StatsClient(RestTemplate rest) {
        this.rest = rest;
    }
}
