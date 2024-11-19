package com.SupportHub.demo.mappers;

import org.springframework.stereotype.Component;

import com.SupportHub.demo.dtos.InputDTOs.UserInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.UserOutputDTO;
import com.SupportHub.demo.models.User;

@Component
public class UserMapper {

    // Convert User Entity to UserOutputDTO
    public UserOutputDTO toDto(User user) {
        UserOutputDTO dto = new UserOutputDTO();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    // Convert UserInputDTO to User Entity
    public User toEntity(UserInputDTO userInputDTO) {
        User user = new User();
        user.setName(userInputDTO.getName());
        user.setEmail(userInputDTO.getEmail());
        user.setRole(userInputDTO.getRole());
        user.setPassword(userInputDTO.getPassword());  
        return user;
    }
}
