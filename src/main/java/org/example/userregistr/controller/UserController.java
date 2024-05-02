package org.example.userregistr.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userregistr.model.dtos.request.UserCreateDto;
import org.example.userregistr.model.dtos.UserDto;
import org.example.userregistr.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{email}")
    public UserDto getUser(@PathVariable("email") String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserDto> getAllUsers(Authentication authentication) {
        log.info("User: {}{}", authentication.getName(), authentication.getAuthorities());
        return userService.getAllUsers();
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/manager")
    public String managerPage() {
        return "Hi manager";
    }
}
