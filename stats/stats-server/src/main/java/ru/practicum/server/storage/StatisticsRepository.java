package ru.practicum.server.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.StatisticsForListDto;
import ru.practicum.server.model.Statistics;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<Statistics, Integer> {

    Statistics findByIpAndUriAndApp(String ip, String uri, String app);

    List<Statistics> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT DISTINCT new ru.practicum.dto.StatisticsForListDto (s.app, s.uri, CAST(COUNT(DISTINCT s.ip) AS int) AS hits)" +
            " FROM ru.practicum.server.model.Statistics AS s " +
            " WHERE s.uri IN (:uris) " +
            " AND timestamp BETWEEN :createdStart AND :createdEnd " +
            " GROUP BY s.app, s.uri ")
    List<StatisticsForListDto> getByCreatedBetweenAndUriInAndUniqueIp(
            @Param("createdStart") LocalDateTime createdStart,
            @Param("createdEnd") LocalDateTime createdEnd,
            @Param("uris") String[] uris);

    @Query(value = "SELECT DISTINCT new ru.practicum.dto.StatisticsForListDto (s.app, s.uri, CAST(COUNT(DISTINCT s.ip) AS int) AS hits)" +
            " FROM ru.practicum.server.model.Statistics AS s " +
            " WHERE timestamp BETWEEN :createdStart AND :createdEnd " +
            " GROUP BY s.app, s.uri ")
    List<StatisticsForListDto> getByCreatedBetweenAndUniqueIp(
            @Param("createdStart") LocalDateTime createdStart,
            @Param("createdEnd") LocalDateTime createdEnd);
}