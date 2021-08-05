package com.okdev.ems.services;

import com.okdev.ems.dto.UserDTO;
import com.okdev.ems.exceptions.EmsAuthException;
import com.okdev.ems.exceptions.EmsBadRequestException;
import com.okdev.ems.exceptions.EmsResourceNotFoundException;
import com.okdev.ems.models.Currencies;
import com.okdev.ems.models.Users;
import com.okdev.ems.models.enums.UserRole;
import com.okdev.ems.repositories.CurrencyRepository;
import com.okdev.ems.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    @Transactional(readOnly = true)
    public Long countAllUsers() throws EmsResourceNotFoundException {
        try {
            return userRepository.countUsersByRole(UserRole.USER);
        } catch (NullPointerException npe) {
            throw new EmsResourceNotFoundException("Users not found");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers(Pageable pageable) {
        List<UserDTO> usersList = userRepository.findUsersByRole(UserRole.USER, pageable)
                .stream().map(Users::toDTO).collect(Collectors.toList());
        return usersList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findByPattern(String pattern) {
        List<UserDTO> usersList = userRepository.findByPattern(pattern)
                .stream().map(Users::toDTO).collect(Collectors.toList());
        return usersList;
    }

    @Override
    @Transactional
    public Users validateUser(String email, String password) throws EmsAuthException {
        if (email != null) email = email.toLowerCase();
        try {
            UserDetails user = userDetailsService.loadUserByUsername(email);
            if (!passwordEncoder.matches(password, user.getPassword()))
                throw new EmsAuthException("Invalid password");
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
    @Transactional
    public void deleteUsers(List<Long> usersId) throws EmsResourceNotFoundException {
        usersId.forEach((u) -> userRepository.deleteById(u));
    }

    @Override
    public Long getUserId(HttpServletRequest request) {
        try {
            return (Long) request.getAttribute("userId");
        } catch (NullPointerException npe) {
            throw new EmsAuthException("User ID incorrect request");
        }
    }
}
