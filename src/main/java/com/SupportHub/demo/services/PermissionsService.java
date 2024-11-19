package com.SupportHub.demo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SupportHub.demo.dtos.InputDTOs.PermissionInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.PermissionsOutputDTO;
import com.SupportHub.demo.exceptions.ResourceNotFoundException;
import com.SupportHub.demo.mappers.PermissionsMapper;
import com.SupportHub.demo.models.Department;
import com.SupportHub.demo.models.Permissions;
import com.SupportHub.demo.models.Project;
import com.SupportHub.demo.models.User;
import com.SupportHub.demo.repositories.PermissionsRepository;

@Service
public class PermissionsService {

    @Autowired
    private PermissionsRepository permissionsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private PermissionsMapper permissionsMapper;

    /**
     * Creates a new Permission entry.
     */
    public PermissionsOutputDTO createPermission(PermissionInputDTO inputDTO) {
        User user = userService.findById(inputDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + inputDTO.getUserId()));
        Department department = departmentService.findById(inputDTO.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + inputDTO.getDepartmentId()));
        Project project = null;
        if (inputDTO.getProjectId() != null) {
            project = projectService.findById(inputDTO.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + inputDTO.getProjectId()));
        }

        Permissions permissions = permissionsMapper.toEntity(inputDTO, user, department, project);
        Permissions savedPermissions = permissionsRepository.save(permissions);
        return permissionsMapper.toDto(savedPermissions);
    }

    /**
     * Retrieves a Permission by its ID.
     */
    public PermissionsOutputDTO getPermissionById(Long id) {
        Permissions permissions = permissionsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id " + id));
        return permissionsMapper.toDto(permissions);
    }

    /**
     * Retrieves all Permissions entries, converting them to DTOs.
     */
    public List<PermissionsOutputDTO> getAllPermissions() {
        List<Permissions> permissionsList = permissionsRepository.findAll();
        return permissionsList.stream()
                .map(permissionsMapper::toDto)
                .collect(Collectors.toList());
    }

    public PermissionsOutputDTO patchPermission(Long id, PermissionInputDTO inputDTO) {
        Permissions existingPermissions = permissionsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id " + id));
        
        // Only update canAnswer if it's provided in the input DTO
        if (inputDTO.getCanAnswer() != null) {
            existingPermissions.setCanAnswer(inputDTO.getCanAnswer());
        }
        
        Permissions updatedPermissions = permissionsRepository.save(existingPermissions);
        return permissionsMapper.toDto(updatedPermissions);
    }

    /**
     * Updates an existing Permission entry.
     */
    public PermissionsOutputDTO updatePermission(Long id, PermissionInputDTO inputDTO) {
        Permissions existingPermissions = permissionsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id " + id));

        User user = userService.findById(inputDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + inputDTO.getUserId()));
        Department department = departmentService.findById(inputDTO.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + inputDTO.getDepartmentId()));
        Project project = null;
        if (inputDTO.getProjectId() != null) {
            project = projectService.findById(inputDTO.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + inputDTO.getProjectId()));
        }

        existingPermissions.setUser(user);
        existingPermissions.setDepartment(department);
        existingPermissions.setProject(project);
        existingPermissions.setCanAnswer(inputDTO.getCanAnswer());

        Permissions updatedPermissions = permissionsRepository.save(existingPermissions);
        return permissionsMapper.toDto(updatedPermissions);
    }

    /**
     * Deletes a Permission entry by its ID.
     */
    public void deletePermission(Long id) {
        Permissions permissions = permissionsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id " + id));
        permissionsRepository.delete(permissions);
    }

    
}
