package com.okdev.ems.controllers;

import com.okdev.ems.config.jwt.JwtProvider;
import com.okdev.ems.dto.TokenDTO;
import com.okdev.ems.dto.UserDTO;
import com.okdev.ems.models.Users;
import com.okdev.ems.models.enums.UserRole;
import com.okdev.ems.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users Controller", description = "Controller for authentication, registration and updating Users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtProvider jwtProvider;

    @GetMapping("/id")
    @Operation(summary = "Get User Parameters", description = "Allows to get a user parameters"
            , security = @SecurityRequirement(name = "bearerAuth"))
//    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UserDTO> getUser(HttpServletRequest request) {
        Long userId = userService.getUserId(request);
        UserDTO userDTO = userService.findById(userId).toDTO();
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/auth")
    @Operation(summary = "User Authentication", description = "Allows to authenticate a user")
    public ResponseEntity<TokenDTO> loginUser(@RequestBody Map<String,Object> userMap) {
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        Users user = userService.validateUser(email, password);
        return new ResponseEntity<>(jwtProvider.generateToken(user), HttpStatus.OK);
    }

    @PostMapping("/register")
    @Operation(summary = "User Registration", description = "Allows to register a user")
    public ResponseEntity<UserDTO> registerUser(@RequestBody Map<String, Object> userMap) {
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        UserDTO user = userService.addUser(firstName, lastName, email, password, UserRole.USER);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/id")
    @Operation(summary = "Update User Parameters", description = "Allows to update a user parameters"
            , security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDTO> editUser(HttpServletRequest request,
                                            @RequestBody UserDTO userDTO) {
        Long userId = userService.getUserId(request);
        UserDTO user = userService.editUser(userId, userDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
