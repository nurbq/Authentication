package org.example.userregistr.dao.repository;

import lombok.RequiredArgsConstructor;
import org.example.userregistr.dao.entity.RefreshToken;
import org.example.userregistr.dao.rowmapper.RefreshTokenRowMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    @Qualifier("primaryJdbcTemplate")
    private final JdbcTemplate jdbcTemplate;

    public Long insert(RefreshToken refreshToken) {
        String query = "select nextval('refresh_token_int_seq')";
        Long id = jdbcTemplate.queryForObject(query, Long.class);
        query = "insert into refresh_token (id, token, expiry_date, user_id) " +
                "values (?, ?, ?, ?)";

        jdbcTemplate.update(query,
                id,
                refreshToken.getToken(),
                Timestamp.from(refreshToken.getExpiryDate()),
                refreshToken.getUserId());
        return id;
    }

    public Optional<RefreshToken> findByToken(String token) {
        String query = "select id, token, expiry_date, user_id from refresh_token where token = ?";

        return Optional.ofNullable(jdbcTemplate.queryForObject(query, new RefreshTokenRowMapper(), token));
    }

    public void delete(RefreshToken refreshToken) {
        String query = "delete from refresh_token where id = ?";
        jdbcTemplate.update(query, refreshToken.getId());
    }
}
