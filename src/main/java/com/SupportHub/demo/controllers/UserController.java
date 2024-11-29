package com.SupportHub.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SupportHub.demo.dtos.InputDTOs.UserInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.UserOutputDTO;
import com.SupportHub.demo.models.User;
import com.SupportHub.demo.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/users/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<UserOutputDTO> getUserById(@PathVariable Long userId) {
        Optional<UserOutputDTO> user = userService.findUserById(userId);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // GET /api/users
    @GetMapping
    public ResponseEntity<List<UserOutputDTO>> getAllUsers() {
        List<UserOutputDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    // POST /api/users
    @PostMapping
    public ResponseEntity<UserOutputDTO> createUser(@RequestBody UserInputDTO userInputDTO) {
        User createdUser = userService.createUser(userInputDTO);
        UserOutputDTO userOutputDTO = userService.getUserMapper().toDto(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userOutputDTO);
    }
    

    // PUT /api/users/{userId}
    @PutMapping("/{userId}")
    public ResponseEntity<UserOutputDTO> updateUser(@PathVariable Long userId, @RequestBody UserInputDTO userInputDTO) {
        try {
            UserOutputDTO updatedUser = userService.updateUser(userId, userInputDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE /api/users/{userId}
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
