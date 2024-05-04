package org.example.userregistr.config.security;

import lombok.RequiredArgsConstructor;
import org.example.userregistr.dao.entity.UserEntity;
import org.example.userregistr.dao.repository.RoleRepository;
import org.example.userregistr.dao.repository.UserRepository;
import org.example.userregistr.exception.IllegalArgumentException;
import org.example.userregistr.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

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
                    .authorities(new SimpleGrantedAuthority(authorities.toString())).build();
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Not found");
        }
    }

}
