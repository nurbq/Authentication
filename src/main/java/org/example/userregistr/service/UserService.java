package org.example.userregistr.service;

import lombok.RequiredArgsConstructor;
import org.example.userregistr.dao.entity.Role;
import org.example.userregistr.dao.entity.UserEntity;
import org.example.userregistr.dao.repository.RoleRepository;
import org.example.userregistr.dao.repository.UserRepository;
import org.example.userregistr.exception.ConflictException;
import org.example.userregistr.exception.IllegalArgumentException;
import org.example.userregistr.exception.NotFoundException;
import org.example.userregistr.kafka.KafkaSender;
import org.example.userregistr.kafka.UserEvent;
import org.example.userregistr.model.dtos.request.UserCreateDto;
import org.example.userregistr.model.dtos.UserDto;
import org.example.userregistr.model.enums.UserRoles;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("passwordEncoder")
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final KafkaSender kafkaSender;


    @Transactional
    public Long userRegister(UserCreateDto userCreateDto) {

        if (userRepository.checkForExistenceUserByEmail(userCreateDto.email())) {
            throw new ConflictException("user already exists");
        }
        Long userId = userRepository.insert(new UserEntity(
                null,
                userCreateDto.email(),
                passwordEncoder.encode(userCreateDto.password()),
                LocalDateTime.now()
        ));

        roleRepository.insert(new Role(null, UserRoles.USER.name(), userId));

        kafkaSender.sendUserEvent(new UserEvent(userCreateDto.email(), userCreateDto.password()), userCreateDto.email());
        return userId;
    }

    public UserDto getUserById(Long userId) {
        UserEntity userFromDb = userRepository.getUserById(userId);
        return new UserDto(userFromDb.getId(), userFromDb.getEmail(), userFromDb.getCreatedTime());
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
