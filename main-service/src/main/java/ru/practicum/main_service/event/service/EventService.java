package ru.practicum.main_service.event.service;

import ru.practicum.main_service.event.comment.dto.CommentDto;
import ru.practicum.main_service.event.comment.dto.CommentNewDto;
import ru.practicum.main_service.event.comment.model.Comment;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.event.dto.EventSmallDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventDto getEventById(int id, HttpServletRequest request);

    int getViews(int eventId);

    List<EventSmallDto> getAllEvents(String text,
                                     List<Integer> categories,
                                     Boolean paid,
                                     LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd,
                                     Boolean onlyAvailable,
                                     String sort,
                                     int from,
                                     int size,
                                     HttpServletRequest httpServletRequest);

    CommentDto addComment(int eventId, int userId, CommentNewDto dto);

    CommentDto editComment(int eventId, int commentId, int userId, CommentNewDto dto);

    Comment getComment(int commentId);

    void deleteComment(int commentId, int userId);

    List<CommentDto> getCommentsByEvent(int eventId, int from, int size);

    EventDto getMostDiscussed();
}
