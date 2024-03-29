package ru.practicum.main_service.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_service.category.model_and_dto.Category;
import ru.practicum.main_service.location.model.Location;
import ru.practicum.main_service.user.model_and_dto.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String annotation;

    @NotEmpty
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @NotNull
    @Column(name = "paid")
    private boolean paid;

    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "event_date")
    private LocalDateTime eventDate;

    private int views;

    @Column(name = "participant_limit")
    private int participantLimit;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @NotNull
    @Column(name = "request_moderation")
    private boolean requestModeration;

    private int confirmedRequests;
}