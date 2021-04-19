package com.okdev.ems.controllers;

import com.okdev.ems.config.jwt.JwtProvider;
import com.okdev.ems.dto.UserDTO;
import com.okdev.ems.models.Users;
import com.okdev.ems.models.enums.UserRole;
import com.okdev.ems.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/register_test")
    public String registerUserTest(@RequestBody Map<String, Object> userMap) {
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        return firstName + ", " + lastName + ", " + email + ", " + password;
    }

    @PostMapping("/auth")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String,Object> userMap) {
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

//    @RequestMapping("/login")
//    public String loginPage() {
//        return "login";
//    }
}
