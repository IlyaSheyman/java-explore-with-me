package ru.practicum.main_service.event.comment.storage;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main_service.event.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByEventId(int eventId, PageRequest pageRequest);

    @Query("SELECT c.event.id FROM Comment c GROUP BY c.event.id ORDER BY COUNT(c.event.id) DESC")
    List<Integer> findMostDiscussed();
}
