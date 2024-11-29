package com.SupportHub.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SupportHub.demo.models.User;
import com.SupportHub.demo.repositories.UserRepository;
import com.SupportHub.demo.dtos.OutputDTOs.UserOutputDTO;
import com.SupportHub.demo.dtos.InputDTOs.UserInputDTO;
import com.SupportHub.demo.mappers.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Optional<UserOutputDTO> findUserById(Long userId) {
        return userRepository.findById(userId).map(userMapper::toDto);
    }

    public List<UserOutputDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserOutputDTO createUser(UserInputDTO userInputDTO) {
        User user = userMapper.toEntity(userInputDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserOutputDTO updateUser(Long userId, UserInputDTO userInputDTO) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setName(userInputDTO.getName());
                    user.setEmail(userInputDTO.getEmail());
                    user.setRole(userInputDTO.getRole());
                    user.setPassword(userInputDTO.getPassword());
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
}
