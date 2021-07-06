package com.okdev.ems.services;

import com.okdev.ems.exceptions.EmsAuthException;
import com.okdev.ems.models.Users;
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

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws EmsAuthException {
        Users user = userService.findByLogin(email);
        if (user == null)
            throw new EmsAuthException(email + " not found");
        List<GrantedAuthority> roles = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()));
        return new User(user.getEmail(), user.getPassword(), roles);
    }
}
