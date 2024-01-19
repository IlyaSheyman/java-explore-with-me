package ru.practicum.server.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.model.Statistics;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<Statistics, Integer> {

    Statistics findByIpAndUriAndApp(String ip, String uri, String app);

    List<Statistics> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
