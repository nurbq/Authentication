package org.example.userregistr.dao.repository;

import lombok.RequiredArgsConstructor;
import org.example.userregistr.dao.entity.UserEntity;
import org.example.userregistr.dao.rowmapper.UserEntityRowMapper;
import org.example.userregistr.exception.IllegalArgumentException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    @Qualifier("primaryJdbcTemplate")
    private final JdbcTemplate jdbcTemplate;

    public Long insert(UserEntity userEntity) {
        String query = "select nextval('users_id_seq')";
        Long id = jdbcTemplate.queryForObject(query, Long.class);
        query = "insert into users (id, email, password, created_time) " +
                "values (?, ?, ?, ?)";

        jdbcTemplate.update(query,
                id,
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getCreatedTime());

        return id;
    }

    public UserEntity getUserByEmail(String email) throws IllegalArgumentException {
        try {
            String query = "select id, email, password, created_time " +
                    "from users " +
                    "where email = ?";

            return jdbcTemplate.queryForObject(
                    query,
                    new UserEntityRowMapper(),
                    email);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("not found email: " + email);
        }
    }

    public List<String> getUserRolesByEmail(String email) {
        String query = "select r.role_name " +
                "from users as u " +
                "inner join roles r on u.id = r.user_id " +
                "where u.email = ?";

        return jdbcTemplate.queryForList(query, String.class, email);
    }

    public List<UserEntity> getAllUsers() {
        String query = "select id, email, password, created_time " +
                "from users";

        return jdbcTemplate.query(query, new UserEntityRowMapper());
    }

    public String deleteUserByEmail(String email) {
        String query = "delete from users where email = ?";

        jdbcTemplate.update(query, email);

        return email;
    }

    public boolean checkForExistenceUserByEmail(String email) {
        String query = "select count(*) " +
                "from users " +
                "where email = ?";

        Integer count = jdbcTemplate.queryForObject(query, Integer.class, email);

        return count != null && count > 0;
    }
}
