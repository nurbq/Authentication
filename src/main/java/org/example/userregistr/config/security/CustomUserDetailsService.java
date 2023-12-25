package org.example.userregistr.config.security;

import org.example.userregistr.dao.entity.UserEntity;
import org.example.userregistr.dao.repository.UserRepository;
import org.example.userregistr.exception.IllegalArgumentException;
import org.example.userregistr.exception.NotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    public CustomUserDetailsService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user;

        try {
            user = userRepository.getUserByEmail(email);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException(e.getMessage());
        }

        return User.builder()
                .username(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();
    }
}
