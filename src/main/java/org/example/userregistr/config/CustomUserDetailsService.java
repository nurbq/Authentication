package org.example.userregistr.config;

import lombok.RequiredArgsConstructor;
import org.example.userregistr.dao.entity.UserEntity;
import org.example.userregistr.dao.repository.RoleRepository;
import org.example.userregistr.dao.repository.UserRepository;
import org.example.userregistr.exception.IllegalArgumentException;
import org.example.userregistr.exception.NotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    //    @Qualifier("passwordEncoder")
//    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserEntity user = userRepository.getUserByEmail(username);
            List<String> authorities = roleRepository.getRolesByUserId(user.getId());
            return User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .authorities(authorities.toString()).build();
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Not found");
        }
    }
}
