package org.example.userregistr.model.dtos.request;

import jakarta.validation.constraints.NotNull;

public record UserCreateDto(
        @NotNull String email,
        @NotNull String password) {
}
