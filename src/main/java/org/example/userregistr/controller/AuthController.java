//package org.example.userregistr.controller;
//
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.example.userregistr.config.jwt.JwtService;
//import org.example.userregistr.dao.entity.RefreshToken;
//import org.example.userregistr.exception.IllegalArgumentException;
//import org.example.userregistr.model.dtos.request.AuthRequestDto;
//import org.example.userregistr.model.dtos.request.RefreshTokenRequestDto;
//import org.example.userregistr.model.dtos.request.UserCreateDto;
//import org.example.userregistr.model.dtos.response.JwtResponseDto;
//import org.example.userregistr.model.dtos.response.RefreshTokenResponseDto;
//import org.example.userregistr.service.RefreshTokenService;
//import org.example.userregistr.service.UserService;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/auth")
//public class AuthController {
//
//    private final UserService userService;
//    private final AuthenticationManager authenticationManager;
//    private final JwtService jwtService;
//    private final RefreshTokenService refreshTokenService;
//
//    @PostMapping("/register")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Long userRegister(@RequestBody @Valid UserCreateDto userCreateDto) {
//        return userService.userRegister(userCreateDto);
//    }
//
//    @PostMapping("/v1/login")
//    public RefreshTokenResponseDto authenticateAndGetToken(@RequestBody AuthRequestDto requestDto) throws IllegalArgumentException {
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword()));
//        if (authentication.isAuthenticated()) {
//            RefreshToken refreshToken = refreshTokenService.createRefreshToken(requestDto.getUsername());
//            return RefreshTokenResponseDto.builder()
//                    .accessToken(jwtService.GenerateToken(requestDto.getUsername()))
//                    .token(refreshToken.getToken())
//                    .build();
//        } else {
//            throw new UsernameNotFoundException("Invalid user request");
//        }
//    }
//
//    @PostMapping("/refreshToken")
//    public RefreshTokenResponseDto refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDTO){
//        return refreshTokenService.findByToken(refreshTokenRequestDTO.getRefreshToken())
//                .map(refreshTokenService::verifyExpiration)
//                .map(RefreshToken::getUserId)
//                .map(userService::getUserById)
//                .map(userInfo -> {
//                    String accessToken = jwtService.GenerateToken(userInfo.email());
//                    return RefreshTokenResponseDto.builder()
//                            .accessToken(accessToken)
//                            .token(refreshTokenRequestDTO.getRefreshToken()).build();
//                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));
//    }
//
//    @PreAuthorize("hasAuthority('ADASDASD')")
//    @GetMapping("/ping")
//    public String ping() {
//        return "Welcome";
//    }
//}
