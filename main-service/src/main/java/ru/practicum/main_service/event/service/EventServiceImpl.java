package ru.practicum.main_service.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.StatisticsForListDto;
import ru.practicum.main_service.admin.service.AdminService;
import ru.practicum.main_service.event.comment.dto.CommentDto;
import ru.practicum.main_service.event.comment.dto.CommentMapper;
import ru.practicum.main_service.event.comment.dto.CommentNewDto;
import ru.practicum.main_service.event.comment.model.Comment;
import ru.practicum.main_service.event.comment.storage.CommentRepository;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.event.dto.EventMapper;
import ru.practicum.main_service.event.dto.EventSmallDto;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.EventState;
import ru.practicum.main_service.event.storage.EventRepository;
import ru.practicum.main_service.exception.IncorrectRequestException;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.user.model_and_dto.User;
import ru.practicum.main_service.user.storage.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Qualifier("AdminServiceImpl")
    private final AdminService adminService;
    private final StatsClient statsClient;

    @Override
    public EventDto getEventById(int id, HttpServletRequest request) {
        statsClient.postStats(request);
        Event event = eventRepository
                .findById(id).orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Event with id " + id + " not found");
        }

        int uniqueViews = getViews(id);
        event.setViews(uniqueViews);
        updateViewsByEventId(id, uniqueViews);

        return eventMapper.toEventDto(event);
    }

    private void updateViewsByEventId(int id, int views) {
        eventRepository.updateViewsById(views, id);
    }

    @Override
    public int getViews(int eventId) {
        List<StatisticsForListDto> response = statsClient.getStats(
                LocalDateTime.now().minusYears(2),
                LocalDateTime.now(),
                new String[]{"/events/" + eventId},
                true);

        return !response.isEmpty() ? response.get(0).getHits() : 0;
    }

    @Override
    public List<EventSmallDto> getAllEvents(String text,
                                            List<Integer> categories,
                                            Boolean paid,
                                            LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd,
                                            Boolean onlyAvailable,
                                            String sort,
                                            int from,
                                            int size,
                                            HttpServletRequest httpServletRequest) {
        statsClient.postStats(httpServletRequest);
        List<Event> allEvents = eventRepository.findAll(PageRequest.of(from / size, size)).toList();

        if (text != null) {
            allEvents.stream()
                    .filter(event -> event.getAnnotation().contains(text) || event.getDescription().contains(text))
                    .collect(Collectors.toList());
        }

        if (categories != null && !categories.isEmpty()) {
            allEvents.stream()
                    .filter(event -> categories.contains(event.getCategory().getId()))
                    .collect(Collectors.toList());
        }

        if (paid != null) {
            allEvents.stream()
                    .filter(event -> event.isPaid() == paid)
                    .collect(Collectors.toList());
        }

        if (rangeStart != null && rangeEnd != null) {
            adminService.validateTimeRange(rangeStart, rangeEnd);
            allEvents.stream()
                    .filter(event -> event.getEventDate().isAfter(rangeStart)
                            && event.getEventDate().isBefore(rangeEnd))
                    .collect(Collectors.toList());
        }

        if (onlyAvailable != false) {
            allEvents.stream()
                    .filter(event -> event.getParticipantLimit() != event.getConfirmedRequests())
                    .collect(Collectors.toList());
        }

        if (sort != null) {
            if (sort.equals("EVENT_DATE")) {
                allEvents.stream()
                        .sorted(Comparator.comparing(Event::getEventDate))
                        .collect(Collectors.toList());
            } else if (sort.equals("VIEWS")) {
                allEvents.stream()
                        .sorted(Comparator.comparingInt(Event::getViews).reversed())
                        .collect(Collectors.toList());
            }
        }

        List<EventSmallDto> filteredEvents = allEvents
                .stream()
                .map(eventMapper::toEventSmallDto)
                .collect(Collectors.toList());

        return filteredEvents;
    }

    @Override
    public CommentDto addComment(int eventId, int userId, CommentNewDto dto) {
        Event event = getEvent(eventId);
        User user = getUser(userId);

        Comment comment = commentMapper.fromCommentNewDto(dto);
        comment.setEvent(event);
        comment.setAuthor(user);

        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    private User getUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }

    private Event getEvent(int eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found"));
    }

    @Override
    public CommentDto editComment(int eventId, int commentId, int userId, CommentNewDto dto) {
        getEvent(eventId);
        getUser(userId);

        Comment comment = getComment(commentId);

        comment.setText(dto.getText());

        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public Comment getComment(int commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id " + commentId + " not found"));
    }

    @Override
    public void deleteComment(int commentId, int userId) {
        User user = getUser(userId);
        Comment comment = getComment(commentId);

        if (comment.getAuthor().getId() != user.getId()) {
            throw new IncorrectRequestException("Only comment's author can delete it");
        } else {
            commentRepository.deleteById(commentId);
        }
    }

    @Override
    public List<CommentDto> getCommentsByEvent(int eventId, int from, int size) {
        getEvent(eventId);
        return commentRepository
                .findAllByEventId(eventId, PageRequest.of(from / size, size))
                .stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventDto getMostDiscussed() {
        List<Integer> ids = commentRepository.findMostDiscussed();
        Integer eventId = ids.get(0);

        if (eventId != null) {
            return eventMapper.toEventDto(getEvent(eventId));
        } else {
            throw new NotFoundException("Most discussed event is not found");
        }
    }
}