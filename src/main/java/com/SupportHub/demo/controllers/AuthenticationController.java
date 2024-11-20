package com.SupportHub.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import com.SupportHub.demo.dtos.AuthenticationRequest;
import com.SupportHub.demo.dtos.AuthenticationResponse;
import com.SupportHub.demo.services.AuthenticationService;
import com.SupportHub.demo.Utils.JwtUtil;

@RestController
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationManager authenticationManager;
    private final AuthenticationService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthenticationController(AuthenticationManager authenticationManager, AuthenticationService userDetailsService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping(value = "/authenticated")
    public ResponseEntity<Object> authenticated(Authentication authentication, Principal principal) {
        return ResponseEntity.ok().body(principal);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            // Authenticate credentials
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );

            // Load user details and generate token
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);

            // Prepare response with token
            AuthenticationResponse response = new AuthenticationResponse(jwt);

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            logger.warn("Authentication failed: Incorrect username or password", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException || e instanceof NullPointerException) {
                logger.error("An error occurred in authentication due to invalid data: ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication failed due to invalid data");
            } else {
                logger.error("Authentication failed due to server error: ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication failed due to server error");
            }
        }
    }

    // Handle unexpected exceptions globally for this controller
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Unexpected error occurred: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
    }
}
