package com.SupportHub.demo.mappers;

import org.springframework.stereotype.Component;
import com.SupportHub.demo.models.Permissions;
import com.SupportHub.demo.models.User;
import com.SupportHub.demo.models.Department;
import com.SupportHub.demo.models.Project;
import com.SupportHub.demo.dtos.OutputDTOs.PermissionsOutputDTO;
import com.SupportHub.demo.dtos.InputDTOs.PermissionInputDTO; // Corrected import

@Component
public class PermissionsMapper {

    // Convert Permissions Entity to PermissionsOutputDTO
    public PermissionsOutputDTO toDto(Permissions permissions) {
        PermissionsOutputDTO dto = new PermissionsOutputDTO();
        dto.setPermissionId(permissions.getPermissionId());
        dto.setUserId(permissions.getUser().getUserId());
        dto.setDepartmentId(permissions.getDepartment().getDepartmentId());
        if (permissions.getProject() != null) {
            dto.setProjectId(permissions.getProject().getProjectId());
        }
        dto.setCanAnswer(permissions.getCanAnswer());
        return dto;
    }

    // Convert PermissionsInputDTO to Permissions Entity
    public Permissions toEntity(PermissionInputDTO permissionsInputDTO, User user, Department department, Project project) { // Corrected method parameter
        Permissions permissions = new Permissions();
        permissions.setUser(user);  // User entity already retrieved
        permissions.setDepartment(department);  // Department entity already retrieved
        permissions.setProject(project);  // Project entity already retrieved
        permissions.setCanAnswer(permissionsInputDTO.getCanAnswer());
        return permissions;
    }
}
