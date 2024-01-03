package org.example.userregistr.dao.repository;

import org.example.userregistr.dao.entity.Role;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepository {

    @Qualifier("primaryJdbcTemplate")
    private final JdbcTemplate jdbcTemplate;

    public RoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Long insert(Role role) {
        String query = "select nextval('roles_id_seq')";
        Long id = jdbcTemplate.queryForObject(query, Long.class);
        query = "insert into roles (id, role_name, user_id)";

        jdbcTemplate.update(query,
                id,
                role.getRoleName(),
                role.getUserId());

        return id;
    }
}
