package com.okdev.ems.controllers;

import com.okdev.ems.config.jwt.JwtProvider;
import com.okdev.ems.dto.TokenDTO;
import com.okdev.ems.dto.UserDTO;
import com.okdev.ems.models.Users;
import com.okdev.ems.models.enums.UserRole;
import com.okdev.ems.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtProvider jwtProvider;

    @GetMapping("/id")
    public ResponseEntity<UserDTO> getUser(HttpServletRequest request) {
        Long userId = userService.getUserId(request);
        UserDTO userDTO = userService.findById(userId).toDTO();
        System.out.println("Current User is " + userDTO.getEmail());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/auth")
    public ResponseEntity<TokenDTO> loginUser(@RequestBody Map<String,Object> userMap) {
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        Users user = userService.validateUser(email, password);
        System.out.println("ID=" + user.getUserId() + " role " + user.getRole());
        return new ResponseEntity<>(jwtProvider.generateToken(user), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody Map<String, Object> userMap) {
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        System.out.println(email);
        UserDTO user = userService.addUser(firstName, lastName, email, password, UserRole.USER);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/id")
    public ResponseEntity<UserDTO> editUser(HttpServletRequest request,
                                            @RequestBody UserDTO userDTO) {
        Long userId = userService.getUserId(request);
        UserDTO user = userService.editUser(userId, userDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public User getCurrentUser () {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

//    @RequestMapping("/login")
//    public String loginPage() {
//        return "login";
//    }
}
