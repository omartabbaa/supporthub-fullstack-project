package com.SupportHub.demo.mappers;

import org.springframework.stereotype.Component;

import com.SupportHub.demo.dtos.InputDTOs.AdminInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.AdminOutputDTO;
import com.SupportHub.demo.models.Admin;
import com.SupportHub.demo.models.Business;
import com.SupportHub.demo.models.User;

@Component
public class AdminMapper {

    // Convert Admin Entity to AdminOutputDTO
    public AdminOutputDTO toDto(Admin admin) {
        AdminOutputDTO dto = new AdminOutputDTO();
        dto.setAdminId(admin.getAdminId());
        dto.setUserId(admin.getUser().getUserId());
        dto.setBusinessId(admin.getBusiness().getBusinessId());
        return dto;
    }

    // Convert AdminInputDTO to Admin Entity
    public Admin toEntity(AdminInputDTO adminInputDTO, User user, Business business) {
        Admin admin = new Admin();
        admin.setUser(user);  // User entity already retrieved
        admin.setBusiness(business);  // Business entity already retrieved
        return admin;
    }
}
