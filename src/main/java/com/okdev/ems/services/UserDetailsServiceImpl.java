package com.okdev.ems.services;

import com.okdev.ems.exceptions.EmsAuthException;
import com.okdev.ems.models.Users;
import com.okdev.ems.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws EmsAuthException {
        Users user = userRepository.findUserByEmail(email);
        if (user == null)
            throw new EmsAuthException(email + " not found");
        List<GrantedAuthority> roles = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()));
        return new User(user.getEmail(), user.getPassword(), roles);
    }
}
