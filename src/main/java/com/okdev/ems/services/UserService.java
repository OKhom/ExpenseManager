package com.okdev.ems.services;

import com.okdev.ems.dto.UserDTO;
import com.okdev.ems.exceptions.EmsAuthException;
import com.okdev.ems.models.Users;
import com.okdev.ems.models.enums.UserRole;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    Users validateUser(String email, String password) throws EmsAuthException;

    UserDTO addUser(String firstName, String lastName, String email, String password, UserRole role) throws EmsAuthException;

    Users findByLogin(String email) throws EmsAuthException;

    Users findById(Long id) throws EmsAuthException;

    Long getUserId(HttpServletRequest request) throws EmsAuthException;
}
