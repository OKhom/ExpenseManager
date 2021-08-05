package com.okdev.ems.services;

import com.okdev.ems.dto.UserDTO;
import com.okdev.ems.exceptions.EmsAuthException;
import com.okdev.ems.exceptions.EmsBadRequestException;
import com.okdev.ems.exceptions.EmsResourceNotFoundException;
import com.okdev.ems.models.Users;
import com.okdev.ems.models.enums.UserRole;

import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface UserService {
    Users validateUser(String email, String password) throws EmsAuthException;

    UserDTO addUser(String firstName, String lastName, String email, String password, UserRole role) throws EmsAuthException;

    UserDTO editUser(Long userId, UserDTO userDTO) throws EmsBadRequestException;

    Users findByLogin(String email) throws EmsAuthException;

    Users findById(Long id) throws EmsAuthException;

    Long getUserId(HttpServletRequest request) throws EmsAuthException;

    Long countAllUsers() throws EmsResourceNotFoundException;

    List<UserDTO> getAllUsers(Pageable pageable);

    List<UserDTO> findByPattern(String pattern);

    void deleteUsers(List<Long> usersId) throws EmsResourceNotFoundException;
}
