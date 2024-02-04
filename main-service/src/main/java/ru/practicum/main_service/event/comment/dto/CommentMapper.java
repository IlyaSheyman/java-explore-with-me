package ru.practicum.main_service.event.comment.dto;

import org.springframework.stereotype.Component;
import ru.practicum.main_service.event.comment.model.Comment;

import java.time.LocalDateTime;

@Component
public class CommentMapper {

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .eventId(comment.getEvent().getId())
                .authorId(comment.getAuthor().getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }

    public Comment fromCommentNewDto(CommentNewDto dto) {
        return Comment.builder()
                .created(LocalDateTime.now())
                .text(dto.getText())
                .build();
    }
}
