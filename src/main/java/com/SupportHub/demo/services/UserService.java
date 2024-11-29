package com.SupportHub.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.SupportHub.demo.dtos.InputDTOs.UserInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.UserOutputDTO;
import com.SupportHub.demo.mappers.UserMapper;
import com.SupportHub.demo.models.User;
import com.SupportHub.demo.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<UserOutputDTO> findUserById(Long userId) {
        return userRepository.findById(userId).map(userMapper::toDto);
    }

    public List<UserOutputDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    // Corrected method: Now returns User instead of UserOutputDTO
    public User createUser(UserInputDTO userInputDTO) {
        // Check if user already exists
        if (userRepository.findByEmail(userInputDTO.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        // Map DTO to entity
        User user = userMapper.toEntity(userInputDTO);

        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save and return the user entity
        return userRepository.save(user);
    }

    public UserOutputDTO updateUser(Long userId, UserInputDTO userInputDTO) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setName(userInputDTO.getName());
                    user.setEmail(userInputDTO.getEmail());
                    user.setRole(userInputDTO.getRole());

                    // Only update the password if a new password is provided
                    if (userInputDTO.getPassword() != null && !userInputDTO.getPassword().isEmpty()) {
                        // Encode the new password before saving
                        user.setPassword(passwordEncoder.encode(userInputDTO.getPassword()));
                    }

                    return userMapper.toDto(userRepository.save(user));
                }).orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    public void deleteUserById(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }

    public Optional<User> findUserEntityByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserMapper getUserMapper() {
        return userMapper;
    }
}
