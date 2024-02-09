package ru.practicum.main_service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_service.event.comment.dto.CommentDto;
import ru.practicum.main_service.event.comment.dto.CommentNewDto;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.event.dto.EventSmallDto;
import ru.practicum.main_service.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main_service.consts.Consts.TIME_FORMAT;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/events")
public class EventController {

    @Qualifier("EventServiceImpl")
    protected final EventService service;

    @GetMapping("/{id}")
    public EventDto getEventById(@PathVariable int id, HttpServletRequest request) {
        log.info("Request to get event by id has been received. EventId: {}", id);
        return service.getEventById(id, request);
    }

    @GetMapping
    public List<EventSmallDto> getAllEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false)
            @DateTimeFormat(fallbackPatterns = TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(fallbackPatterns = TIME_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size,
            HttpServletRequest httpServletRequest) {

        log.info("Request to get events with filtering options received. " +
                        "Text: {}, " +
                        "Categories: {}, " +
                        "Paid: {}, " +
                        "RangeStart: {}, " +
                        "RangeEnd: {}, " +
                        "OnlyAvailable: {}, " +
                        "Sort: {}, " +
                        "From: {}, " +
                        "Size: {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);


        return service.getAllEvents(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, httpServletRequest);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{eventId}/comment")
    public CommentDto addComment(@RequestHeader(value = "X-User-Id") int userId,
                                 @PathVariable int eventId,
                                 @RequestBody @Valid CommentNewDto dto) {
        log.info("Request to add new comment has been received. UserId: {}, EventId: {}, Comment: {}",
                userId, eventId, dto.toString());

        return service.addComment(eventId, userId, dto);
    }

    @PatchMapping("/{eventId}/comment/{commentId}")
    public CommentDto editComment(@RequestHeader(value = "X-User-Id") int userId,
                                  @PathVariable int eventId,
                                  @PathVariable int commentId,
                                  @RequestBody @Valid CommentNewDto dto) {
        log.info("Request to edit comment has been received. UserId: {}, EventId: {}, CommentId: {}, Updated: {}",
                userId, eventId, commentId, dto.toString());

        return service.editComment(eventId, commentId, userId, dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/comment/{commentId}")
    public void deleteComment(@RequestHeader(value = "X-User-Id") int userId,
                              @PathVariable int commentId) {
        log.info("Request to delete comment has been received. UserId: {}, CommentId: {}", userId, commentId);
        service.deleteComment(commentId, userId);
    }

    @GetMapping("{eventId}/comments")
    public List<CommentDto> getCommentsByEvent(@PathVariable int eventId,
                                               @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                               @RequestParam(value = "size", defaultValue = "10") @Min(1) int size) {
        log.info("Request to get comments by event has been received. EventId: {}, From: {}, Size: {}",
                eventId, from, size);

        return service.getCommentsByEvent(eventId, from, size);
    }

    @GetMapping("/most_discussed")
    public EventDto getMostDiscussed() {
        log.info("Request to get the most discussed event has been received");

        return service.getMostDiscussed();
    }
}