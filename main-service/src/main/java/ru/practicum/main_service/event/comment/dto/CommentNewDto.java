package ru.practicum.main_service.event.comment.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CommentNewDto {
    @Size(min = 3, max = 1000)
    private String text;
}
