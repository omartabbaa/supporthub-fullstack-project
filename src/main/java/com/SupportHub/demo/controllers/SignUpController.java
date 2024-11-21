package com.SupportHub.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.SupportHub.demo.dtos.OutputDTOs.UserOutputDTO;
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

            // Add ROLE_ prefix if not present
            if ("ADMIN".equals(userInputDTO.getRole())) {
                userInputDTO.setRole("ROLE_" + userInputDTO.getRole());
            }

            // Create user with encrypted password
            User user = userService.createUser(userInputDTO);

            if ("ROLE_ADMIN".equals(user.getRole())) {
                if (businessInputDTO == null) {
                    return ResponseEntity.badRequest().body("Business information is required for this role.");
                }
                businessService.createBusinessWithAdmin(businessInputDTO, user);
                entityManager.flush();
            }

            UserOutputDTO userOutputDTO = userMapper.toDto(user);
            return ResponseEntity.ok(userOutputDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error during signup: " + e.getMessage());
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
