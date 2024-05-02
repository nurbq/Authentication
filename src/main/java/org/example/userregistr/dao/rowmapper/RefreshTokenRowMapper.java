package org.example.userregistr.dao.rowmapper;

import org.example.userregistr.dao.entity.RefreshToken;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RefreshTokenRowMapper implements RowMapper<RefreshToken> {
    @Override
    public RefreshToken mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new RefreshToken(
                rs.getLong("id"),
                rs.getString("token"),
                rs.getTimestamp("expiry_date").toInstant(),
                rs.getLong("user_id")
        );
    }
}
