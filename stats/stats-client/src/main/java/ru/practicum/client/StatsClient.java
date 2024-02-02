package ru.practicum.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.StatisticsDto;
import ru.practicum.dto.StatisticsForListDto;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

@Service
public class StatsClient {
    protected final RestTemplate rest;

    @Autowired
    public StatsClient(RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:9090"))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<Object> postStats(HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = LocalDateTime.now().format(formatter);

        StatisticsDto statsDto = StatisticsDto
                .builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(formattedTimestamp)
                .build();

        final String path = "/hit";
        return makeAndSendRequest(HttpMethod.POST, path, null, statsDto);
    }

    public List<StatisticsForListDto> getStats(LocalDateTime start,
                                                               LocalDateTime end,
                                                               String[] uris,
                                                               boolean unique) {
        if (start.isAfter(end)) {
            throw new RuntimeException("Начало не может быть после конца");
        }
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String startFormatted = start.format(formatter);
        String endFormatted = end.format(formatter);

        Map<String, Object> parameters = Map.of(
                "start", startFormatted,
                "end", endFormatted,
                "uris", uris,
                "unique", unique
        );


        ResponseEntity<Object> response = get(parameters);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return List.of(mapper.readValue(
                        mapper.writeValueAsString(response.getBody()),
                        StatisticsForListDto[].class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Ошибка парсинга информации о просмотрах из JSON");
            }
        }
        return null;
    }

    protected ResponseEntity<Object> get(@Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(
                HttpMethod.GET,
                "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                parameters,
                null);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path,
                                                          @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> serverResponse;
        try {
            if (parameters != null) {
                serverResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                serverResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(serverResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}