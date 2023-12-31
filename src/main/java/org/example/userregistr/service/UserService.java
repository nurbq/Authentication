package org.example.userregistr.service;

import lombok.RequiredArgsConstructor;
import org.example.userregistr.dao.entity.UserEntity;
import org.example.userregistr.dao.repository.UserRepository;
import org.example.userregistr.exception.ConflictException;
import org.example.userregistr.exception.IllegalArgumentException;
import org.example.userregistr.exception.NotFoundException;
import org.example.userregistr.model.dtos.UserCreateDto;
import org.example.userregistr.model.dtos.UserDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long userRegister(UserCreateDto userCreateDto) {

        if (userRepository.checkForExistenceUserByEmail(userCreateDto.email())) {
            throw new ConflictException("user already exists");
        }

        return userRepository.insert(new UserEntity(
                null,
                userCreateDto.email(),
                userCreateDto.password(),
                LocalDateTime.now()
        ));
    }

    public UserDto getUserByEmail(String email) {
        UserEntity userEntity;
        try {
            userEntity = userRepository.getUserByEmail(email);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("user not found");
        }
        return new UserDto(userEntity.getId(), userEntity.getEmail(), userEntity.getCreatedTime());
    }

    public List<UserDto> getAllUsers() {
        List<UserEntity> userDtoList = userRepository.getAllUsers();

        if (userDtoList.isEmpty()) {
            throw new NotFoundException("users not found");
        }

        return userDtoList
                .stream()
                .map(userFromDb -> new UserDto(userFromDb.getId(), userFromDb.getEmail(), userFromDb.getCreatedTime()))
                .collect(Collectors.toList());
    }

    public String deleteUserByEmail(String email) {
        UserEntity user;
        try {
            user = userRepository.getUserByEmail(email);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("user not found");
        }

        return userRepository.deleteUserByEmail(user.getEmail());
    }
}
