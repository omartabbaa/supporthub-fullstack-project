package com.SupportHub.demo.services;

import com.SupportHub.demo.models.Admin;
import com.SupportHub.demo.repositories.AdminRepository;
import com.SupportHub.demo.repositories.UserRepository;
import com.SupportHub.demo.repositories.BusinessRepository;
import com.SupportHub.demo.dtos.OutputDTOs.AdminOutputDTO;
import com.SupportHub.demo.dtos.InputDTOs.AdminInputDTO;
import com.SupportHub.demo.mappers.AdminMapper;
import com.SupportHub.demo.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private AdminMapper adminMapper;

    // Find admin by ID
    public Optional<AdminOutputDTO> findAdminById(Long adminId) {
        return adminRepository.findById(adminId)
                .map(adminMapper::toDto);
    }

    // Find all admins
    public List<AdminOutputDTO> findAllAdmins() {
        return adminRepository.findAll().stream()
                .map(adminMapper::toDto)
                .collect(Collectors.toList());
    }

    // Create a new admin
    public AdminOutputDTO createAdmin(AdminInputDTO adminInputDTO) {
        Admin admin = adminMapper.toEntity(
            adminInputDTO,
            userRepository.findById(adminInputDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + adminInputDTO.getUserId())),
            businessRepository.findById(adminInputDTO.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business not found with ID: " + adminInputDTO.getBusinessId()))
        );
        Admin savedAdmin = adminRepository.save(admin);
        return adminMapper.toDto(savedAdmin);
    }

    // Update an existing admin
    public AdminOutputDTO updateAdmin(Long adminId, AdminInputDTO adminInputDTO) {
        return adminRepository.findById(adminId)
                .map(admin -> {
                    admin.setUser(userRepository.findById(adminInputDTO.getUserId())
                            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + adminInputDTO.getUserId())));
                    admin.setBusiness(businessRepository.findById(adminInputDTO.getBusinessId())
                            .orElseThrow(() -> new ResourceNotFoundException("Business not found with ID: " + adminInputDTO.getBusinessId())));
                    Admin updatedAdmin = adminRepository.save(admin);
                    return adminMapper.toDto(updatedAdmin);
                }).orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + adminId));
    }

    // Delete an admin by ID
    public void deleteAdminById(Long adminId) {
        if (adminRepository.existsById(adminId)) {
            adminRepository.deleteById(adminId);
        } else {
            throw new ResourceNotFoundException("Admin not found with ID: " + adminId);
        }
    }
}
