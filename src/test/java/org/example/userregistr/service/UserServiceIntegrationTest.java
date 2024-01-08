package org.example.userregistr.service;


import org.assertj.core.api.Assertions;
import org.example.userregistr.config.IntegrationTestContext;
import org.example.userregistr.dao.entity.UserEntity;
import org.example.userregistr.dao.repository.UserRepository;
import org.example.userregistr.model.dtos.UserCreateDto;
import org.example.userregistr.model.dtos.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class UserServiceIntegrationTest extends IntegrationTestContext {


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

        userRepository.deleteUserByEmail(userCreateDto.email());
    }

    @DisplayName("Successfully retrieve user by email")
    @Test
    void getUserByEmail_WithValidEmail_ReturnsUserDto() {
        UserEntity userEntity = new UserEntity(null, "test123@gmail.com", "test", LocalDateTime.now());
        userRepository.insert(userEntity);

        UserDto userDto = userService.getUserByEmail(userEntity.getEmail());

        Assertions.assertThat(userDto.email()).isEqualTo(userEntity.getEmail());

        userRepository.deleteUserByEmail(userEntity.getEmail());
    }

    @DisplayName("Successfully retrieve all users")
    @Test
    void getAllUsers_ReturnsListUserDto() {
        int listSize = 5;
        List<UserEntity> userEntities = getMockUserList(listSize);
        userEntities.forEach(userEntity -> userRepository.insert(userEntity));

        List<UserDto> userDtos = userService.getAllUsers();

        Assertions.assertThat(userDtos.size()).isEqualTo(listSize);

    }


    private List<UserEntity> getMockUserList(int size) {
        List<UserEntity> userEntityList = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            userEntityList.add(new UserEntity(null, "email" + i, "password" + i, LocalDateTime.now().plusMinutes(i)));
        }
        return userEntityList;
    }
}
