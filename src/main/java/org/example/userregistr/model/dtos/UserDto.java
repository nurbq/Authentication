package org.example.userregistr.model.dtos;

import java.time.LocalDateTime;

public record UserDto(Long id, String email, LocalDateTime createdTime) {
}
