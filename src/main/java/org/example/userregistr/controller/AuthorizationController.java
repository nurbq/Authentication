package org.example.userregistr.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userregistr.model.dtos.UserCreateDto;
import org.example.userregistr.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthorizationController {

    private final UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody @Valid UserCreateDto userCreateDto) {
        return userService.userRegister(userCreateDto);
    }
}
