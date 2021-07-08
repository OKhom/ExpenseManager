package com.okdev.ems.services;

import com.okdev.ems.dto.UserDTO;
import com.okdev.ems.exceptions.EmsAuthException;
import com.okdev.ems.exceptions.EmsBadRequestException;
import com.okdev.ems.models.Currencies;
import com.okdev.ems.models.Users;
import com.okdev.ems.models.enums.UserRole;
import com.okdev.ems.repositories.CurrencyRepository;
import com.okdev.ems.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    @Transactional(readOnly = true)
    public Users findByLogin(String email) throws EmsAuthException {
        return userRepository.findUserByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Users findById(Long id) throws EmsAuthException {
        try {
            return userRepository.findUserById(id);
        } catch (NullPointerException npe) {
            throw new EmsAuthException("User ID not found");
        }
    }

    @Override
    @Transactional
    public Users validateUser(String email, String password) throws EmsAuthException {
        if (email != null) email = email.toLowerCase();
        try {
            UserDetails user = userDetailsService.loadUserByUsername(email);
            if (!passwordEncoder.matches(password, user.getPassword()))
                throw new EmsAuthException("Invalid password");
            System.out.println("Current User is " + user);
            return userRepository.findUserByEmail(user.getUsername());
        } catch (NullPointerException e) {
            throw new EmsAuthException("Invalid email");
        }
    }

    @Override
    @Transactional
    public UserDTO addUser(String firstName, String lastName, String email, String password, UserRole role) throws EmsAuthException {
        try {
            Pattern pattern = Pattern.compile("^(.+)@(.+)$");
            if (email != null) {
                email = email.toLowerCase();
                if (!pattern.matcher(email).matches())
                    throw new EmsAuthException("Invalid email format");
                Long count = userRepository.getCountByEmail(email);
                if (count > 0)
                    throw new EmsAuthException("Email already in use");
                String passHash = passwordEncoder.encode(password);
                Currencies currency = currencyRepository.findByShortName("USD");
                Users user = new Users(firstName, lastName, email, passHash, role, currency);
                userRepository.save(user);
                return user.toDTO();
            } else throw new EmsAuthException("User email not found");
        } catch (NullPointerException npe) {
            throw new EmsAuthException("User email not found");
        }
    }

    @Override
    @Transactional
    public UserDTO editUser(Long userId, UserDTO userDTO) throws EmsBadRequestException {
        try {
            Users user = userRepository.findUserById(userId);
            if (userDTO.getFirstName() != null)
                user.setFirstName(userDTO.getFirstName());
            if (userDTO.getLastName() != null)
                user.setLastName(userDTO.getLastName());
            if (userDTO.getCurrencyId() != null)
                user.setCurrency(currencyRepository.getOne(userDTO.getCurrencyId()));
            userRepository.save(user);
            return user.toDTO();
        } catch (NullPointerException npe) {
            throw new EmsBadRequestException("Edit User: invalid request");
        }
    }

    @Override
    public Long getUserId(HttpServletRequest request) throws EmsAuthException {
        try {
            return (Long) request.getAttribute("userId");
        } catch (NullPointerException npe) {
            throw new EmsAuthException("User ID incorrect request");
        }
    }
}
