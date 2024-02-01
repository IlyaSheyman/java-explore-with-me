package ru.practicum.main_service.compilation.storage;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {
    boolean existsByTitle(String title);

    List<Compilation> findAllByPinned(boolean pinned, PageRequest of);
}
