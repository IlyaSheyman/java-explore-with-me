package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Statistics;

public interface StatisticsRepository extends JpaRepository<Statistics, Integer> {

    Statistics findByIpAndUriAndApp(String ip, String uri, String app);
}
