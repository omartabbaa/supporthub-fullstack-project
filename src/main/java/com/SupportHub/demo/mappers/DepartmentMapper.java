package com.SupportHub.demo.mappers;

import org.springframework.stereotype.Component;

import com.SupportHub.demo.dtos.InputDTOs.DepartmentInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.DepartmentOutputDTO;
import com.SupportHub.demo.models.Business;
import com.SupportHub.demo.models.Department;

@Component
public class DepartmentMapper {

    // Convert Department Entity to DepartmentOutputDTO
    public DepartmentOutputDTO toDto(Department department) {
        DepartmentOutputDTO dto = new DepartmentOutputDTO();
        dto.setDepartmentId(department.getDepartmentId());
        dto.setDepartmentName(department.getDepartmentName());
        dto.setDescription(department.getDescription());
        if (department.getBusiness() != null) {
            dto.setBusinessId(department.getBusiness().getBusinessId());
        }
        return dto;
    }

    // Convert DepartmentInputDTO to Department Entity
    public Department toEntity(DepartmentInputDTO departmentInputDTO, Business business) {
        Department department = new Department();
        department.setDepartmentName(departmentInputDTO.getDepartmentName());
        department.setDescription(departmentInputDTO.getDescription());
        department.setBusiness(business);
        return department;
    }
}
