package org.example.userregistr.model.jwt;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Token(
        UUID id,
        String subject,
        List<String> roles,
        Instant createdAt,
        Instant expiresAt
) {
}
