package org.example.userregistr.dao.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.userregistr.dao.entity.Role;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoleRepository {

    @Qualifier("primaryJdbcTemplate")
    private final JdbcTemplate jdbcTemplate;

    public List<String> getRolesByUserId(Long userId) {
        String query = "select role_name " +
                       "from roles " +
                       "where user_id = ?";

        return jdbcTemplate.queryForList(query, String.class, userId);
    }

    public Long insert(Role role) {
        String query = "select nextval('roles_id_seq')";
        Long id = jdbcTemplate.queryForObject(query, Long.class);
        query = "insert into roles (id, role_name, user_id) " +
                "values (?, ?, ?)";

        jdbcTemplate.update(query,
                id,
                role.getRoleName(),
                role.getUserId());

        return id;
    }

    public void deleteByUserId(Long userId) {
        String query = "delete " +
                       "from roles " +
                       "where user_id = ?";

        jdbcTemplate.update(query, userId);
    }
}
