package org.example.userregistr.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userregistr.model.dtos.UserCreateDto;
import org.example.userregistr.model.dtos.UserDto;
import org.example.userregistr.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{email}")
    public UserDto getUser(@PathVariable("email") String email) {
        return new UserDto(1L, "John", LocalDateTime.now());
    }

    @GetMapping("/all")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserDto> getAllUsers() {
        return null;
    }

    @PostMapping("/register")
    public Long userRegister(@RequestBody @Valid UserCreateDto userCreateDto) {
        return userService.userRegister(userCreateDto);
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "Hello admin";
    }

    @DeleteMapping("/delete/{email}")
    public String deleteUserByEmail(@PathVariable String email) {
        return "deleted";
    }
}
