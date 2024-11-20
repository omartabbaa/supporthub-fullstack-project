package com.SupportHub.demo.services;  // Changed from com.SupportHub.demo.services

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SupportHub.demo.dtos.InputDTOs.DepartmentInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.DepartmentOutputDTO;
import com.SupportHub.demo.mappers.DepartmentMapper;
import com.SupportHub.demo.models.Business;
import com.SupportHub.demo.models.Department;
import com.SupportHub.demo.repositories.BusinessRepository;
import com.SupportHub.demo.repositories.DepartmentRepository;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final BusinessRepository businessRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper, BusinessRepository businessRepository) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.businessRepository = businessRepository;
    }

    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    public Optional<DepartmentOutputDTO> findDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId).map(department -> departmentMapper.toDto(department));
    }

    public List<DepartmentOutputDTO> findAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(department -> departmentMapper.toDto(department))
                .collect(Collectors.toList());
    }

    public DepartmentOutputDTO createDepartment(DepartmentInputDTO departmentInputDTO) {
        Business business = businessRepository.findById(departmentInputDTO.getBusinessId())
            .orElseThrow(() -> new RuntimeException("Business not found with ID: " + departmentInputDTO.getBusinessId()));
            
        Department department = departmentMapper.toEntity(departmentInputDTO, business);
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDto(savedDepartment);
    }

    public DepartmentOutputDTO updateDepartment(Long departmentId, DepartmentInputDTO departmentInputDTO) {
        return departmentRepository.findById(departmentId)
                .map(department -> {
                    department.setDepartmentName(departmentInputDTO.getDepartmentName());
                    department.setDescription(departmentInputDTO.getDescription());
                    
                    if (departmentInputDTO.getBusinessId() != null) {
                        Business business = businessRepository.findById(departmentInputDTO.getBusinessId())
                            .orElseThrow(() -> new RuntimeException("Business not found with ID: " + departmentInputDTO.getBusinessId()));
                        department.setBusiness(business);
                    }
                    
                    return departmentMapper.toDto(departmentRepository.save(department));
                }).orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
    }

    public void deleteDepartmentById(Long departmentId) {
        if (departmentRepository.existsById(departmentId)) {
            departmentRepository.deleteById(departmentId);
        } else {
            throw new RuntimeException("Department not found with ID: " + departmentId);
        }
    }

    public DepartmentOutputDTO patchDepartment(Long id, DepartmentInputDTO inputDTO) {
        Department existingDepartment = departmentRepository.findById(id)
                .map(department -> {
                    if (inputDTO.getDepartmentName() != null) {
                        department.setDepartmentName(inputDTO.getDepartmentName());
                    }
                    if (inputDTO.getDescription() != null) {
                        department.setDescription(inputDTO.getDescription());
                    }
                    return departmentRepository.save(department);
                })
                .orElseThrow(() -> new RuntimeException("Department not found with id " + id));
        return departmentMapper.toDto(existingDepartment);
    }
}