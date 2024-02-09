package ru.practicum.main_service.user.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.user.model_and_dto.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    Page<User> findByIdIn(List<Integer> ids, Pageable pageable);
}
