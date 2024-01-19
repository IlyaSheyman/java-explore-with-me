package ru.practicum.main_service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}/events")
    public void untitled() {

    }

    @PostMapping("/{id}/events")
    public void untitled() {

    }

    @GetMapping("/{id}/events/{eventId}")
    public void untitled() {

    }

    @PatchMapping("/{id}/events/{eventId}")
    public void untitled() {

    }

    @GetMapping("/{id}/events/{eventId}/requests")
    public void untitled() {

    }

    @PatchMapping("/{id}/events/{eventId}/requests")
    public void untitled() {

    }
}
