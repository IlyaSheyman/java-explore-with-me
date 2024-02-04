package ru.practicum.main_service.event.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private int id;
    private String text;
    private int eventId;
    private int authorId;
    private LocalDateTime created;

}