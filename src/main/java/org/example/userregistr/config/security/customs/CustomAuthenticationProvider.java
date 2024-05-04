//package org.example.userregistr.config.security;
//
//import lombok.RequiredArgsConstructor;
//import org.example.userregistr.dao.entity.UserEntity;
//import org.example.userregistr.dao.repository.UserRepository;
//import org.example.userregistr.exception.IllegalArgumentException;
//import org.example.userregistr.exception.NotFoundException;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//
//
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//
//
//    private final UserRepository userRepository;
//
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String authName = authentication.getName();
//        UserEntity userEntity;
//        try {
//            userEntity = userRepository.getUserByEmail(authName);
//        } catch (IllegalArgumentException e) {
//            throw new NotFoundException(e.getMessage());
//        }
//
//        if (authName.equals(userEntity.getEmail())) {
//            if (authentication.getCredentials().equals(userEntity.getPassword())) {
//                UserDetails loginUser = User.withUsername(authName).password("{noop}" + userEntity.getPassword()).authorities("user").build();
//                return new UsernamePasswordAuthenticationToken(loginUser, userEntity.getPassword(), loginUser.getAuthorities());
//            }
//        }
//
//        return null;
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}
