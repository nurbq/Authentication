package org.example.userregistr.service;


import org.assertj.core.api.Assertions;
import org.example.userregistr.dao.entity.UserEntity;
import org.example.userregistr.dao.repository.UserRepository;
import org.example.userregistr.exception.IllegalArgumentException;
import org.example.userregistr.exception.NotFoundException;
import org.example.userregistr.model.dtos.UserCreateDto;
import org.example.userregistr.model.dtos.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // [MethodName][Scenario][ExpectedBehavior]
    @Test
    void createUser_WithValidUser_ReturnsSavedUser() {
        UserCreateDto userCreateDto = new UserCreateDto("email@gmail.com", "password");

        when(userRepository.insert(any(UserEntity.class))).thenReturn(1L);

        Long id = userService.userRegister(userCreateDto);

        Assertions.assertThat(id).isNotNull();
    }

    @Test
    void getUser_WithValidEmail_ReturnsUser() throws IllegalArgumentException {
        String email = "test@gmail.com";
        UserEntity userEntity = new UserEntity(1L, "test@gmail.com", "password", LocalDateTime.now());

        when(userRepository.getUserByEmail(anyString())).thenReturn(userEntity);

        UserDto user = userService.getUserByEmail(email);

        Assertions.assertThat(user.email()).isEqualTo(email);
    }


    @Test
    void getAllUsers_ReturnsAllUsers() {
        List<UserEntity> userEntities = getMockUserEntities(3);

        when(userRepository.getAllUsers()).thenReturn(userEntities);

        List<UserDto> userDtoList = userService.getAllUsers();

        Assertions.assertThat(userDtoList.size()).isEqualTo(3);
    }

    @Test
    void getAllUsers_NoUsers_ThrowsException() {
        List<UserEntity> userEntities = new ArrayList<>();

        when(userRepository.getAllUsers()).thenReturn(userEntities);

        Assertions.assertThatThrownBy(() -> {
            userService.getAllUsers();
        }).isInstanceOf(NotFoundException.class).hasMessageContaining("users not found");
    }

    @Test
    void deleteUser_WithValideEmail_ReturnsEmail() throws IllegalArgumentException {
        String email = "test@gmail.com";
        when(userRepository.deleteUserByEmail(eq(email))).thenReturn(email);

        email = userService.deleteUserByEmail(email);

        Assertions.assertThat(email).isNotNull();

        verify(userRepository, times(1)).deleteUserByEmail(eq(email));
    }


    private List<UserEntity> getMockUserEntities(Integer size) {
        List<UserEntity> userEntities = new ArrayList<>(size);

        for (int i = 1; i <= size; i++) {
            userEntities.add(new UserEntity(Integer.toUnsignedLong(i), i + "test@email.com",
                    "pass" + i, LocalDateTime.now().plusMinutes(i)));
        }

        return userEntities;
    }


}
