package com.SupportHub.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.SupportHub.demo.dtos.InputDTOs.BusinessInputDTO;
import com.SupportHub.demo.dtos.InputDTOs.UserInputDTO;

import com.SupportHub.demo.mappers.UserMapper;
import com.SupportHub.demo.models.User;
import com.SupportHub.demo.services.BusinessService;
import com.SupportHub.demo.services.UserService;

@RestController
@RequestMapping("/api/users")
public class SignUpController {

    @Autowired
    private UserService userService;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private UserMapper userMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signUpRequest) {
        try {
            UserInputDTO userInputDTO = signUpRequest.getUser();
            BusinessInputDTO businessInputDTO = signUpRequest.getBusiness();

            // Validate email doesn't exist before proceeding
            if (userService.findUserEntityByEmail(userInputDTO.getEmail()).isPresent()) {
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Email already registered: " + userInputDTO.getEmail());
            }

            // Standardize role format
            String role = userInputDTO.getRole();
            if (!role.startsWith("ROLE_")) {
                role = "ROLE_" + role;
            }
            userInputDTO.setRole(role);

            // Create user
            User user = userService.createUser(userInputDTO);

            // Handle business creation for admin
            if (role.equals("ROLE_ADMIN")) {
                if (businessInputDTO == null) {
                    return ResponseEntity.badRequest().body("Business information is required for admin role.");
                }
                try {
                    businessService.createBusinessWithAdmin(businessInputDTO, user);
                } catch (Exception e) {
                    // If business creation fails, we should rollback the user creation
                    userService.deleteUserById(user.getUserId());
                    return ResponseEntity.badRequest()
                        .body("Failed to create business: " + e.getMessage());
                }
            }

            return ResponseEntity.ok(userMapper.toDto(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Error during signup: " + e.getMessage());
        }
    }

    // DTO representing the signup request structure
    public static class SignupRequest {
        private UserInputDTO user;
        private BusinessInputDTO business;

        // Getters and Setters
        public UserInputDTO getUser() { return user; }
        public void setUser(UserInputDTO user) { this.user = user; }
        public BusinessInputDTO getBusiness() { return business; }
        public void setBusiness(BusinessInputDTO business) { this.business = business; }
    }
}
