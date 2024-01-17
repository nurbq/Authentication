package org.example.userregistr.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userregistr.model.dtos.UserCreateDto;
import org.example.userregistr.model.dtos.UserDto;
import org.example.userregistr.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{email}")
    public UserDto getUser(@PathVariable("email") String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/all")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserDto> getAllUsers() {
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
        return userService.deleteUserByEmail(email);
    }
}
