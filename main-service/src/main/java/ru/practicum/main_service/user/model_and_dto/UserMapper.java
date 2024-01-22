package ru.practicum.main_service.user.model_and_dto;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User fromUserDto(UserDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .build();
    }

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
