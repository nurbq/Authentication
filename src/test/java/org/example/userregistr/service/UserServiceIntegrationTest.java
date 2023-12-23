package org.example.userregistr.service;


import org.assertj.core.api.Assertions;
import org.example.userregistr.dao.entity.UserEntity;
import org.example.userregistr.dao.repository.UserRepository;
import org.example.userregistr.model.dtos.UserCreateDto;
import org.example.userregistr.model.dtos.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceIntegrationTest {


    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    ).withInitScript("ddl.sql");

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @DisplayName("Successfully creating a new user")
    @Test
    void userRegister_WithValidCredentials_ReturnsUserDto() {
        UserCreateDto userCreateDto = new UserCreateDto("jh@gmail.com", "pass123");

        Long userDto = userService.userRegister(userCreateDto);

        Assertions.assertThat(userDto).isNotNull();
        Assertions.assertThat(userDto).isEqualTo(1L);
    }

    @Test
    void getUserByEmail_WithValidEmail_ReturnsUserDto() {
        UserEntity userEntity = new UserEntity(null, "test123@gmail.com", "test", LocalDateTime.now());
        userRepository.insert(userEntity);

        UserDto userDto = userService.getUser(userEntity.getEmail());

        Assertions.assertThat(userDto.email()).isEqualTo(userEntity.getEmail());
    }


//    @DynamicPropertySource
//    static void postgresqlProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//    }
}
