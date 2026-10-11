package com.example.spacemate.controller;

import com.example.spacemate.dto.UserResponse;
import com.example.spacemate.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse>
    getProfile(Authentication authentication) {

        return ResponseEntity.ok(
                userService.getProfile(
                        authentication.getName()
                )
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse>
    updateProfile(
            @RequestParam String name,
            @RequestParam String phone,
            Authentication authentication) {

        return ResponseEntity.ok(
                userService.updateProfile(
                        authentication.getName(),
                        name,
                        phone
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>>
    getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }
}