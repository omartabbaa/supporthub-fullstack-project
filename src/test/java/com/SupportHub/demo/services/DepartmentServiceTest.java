package com.SupportHub.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.SupportHub.demo.dtos.InputDTOs.DepartmentInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.DepartmentOutputDTO;
import com.SupportHub.demo.exceptions.ResourceNotFoundException;
import com.SupportHub.demo.mappers.DepartmentMapper;
import com.SupportHub.demo.models.Business;
import com.SupportHub.demo.models.Department;
import com.SupportHub.demo.repositories.BusinessRepository;
import com.SupportHub.demo.repositories.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private BusinessRepository businessRepository;

    private DepartmentMapper departmentMapper;
    private DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        departmentMapper = spy(new DepartmentMapper());
        departmentService = new DepartmentService(departmentRepository, departmentMapper, businessRepository);
    }

    @Test
    void testFindAllDepartments() {
        // Arrange
        Department dept1 = new Department();
        Department dept2 = new Department();
        DepartmentOutputDTO deptDTO1 = new DepartmentOutputDTO();
        DepartmentOutputDTO deptDTO2 = new DepartmentOutputDTO();
        
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(dept1, dept2));
        when(departmentMapper.toDto(dept1)).thenReturn(deptDTO1);
        when(departmentMapper.toDto(dept2)).thenReturn(deptDTO2);

        // Act
        List<DepartmentOutputDTO> result = departmentService.findAllDepartments();

        // Assert
        assertEquals(2, result.size());
        verify(departmentRepository).findAll();
    }

    @Test
    void testCreateDepartment() {
        // Arrange
        DepartmentInputDTO inputDTO = new DepartmentInputDTO();
        Business business = new Business();
        Department department = new Department();
        Department savedDepartment = new Department();
        DepartmentOutputDTO outputDTO = new DepartmentOutputDTO();
        
        when(businessRepository.findById(inputDTO.getBusinessId())).thenReturn(Optional.of(business));
        when(departmentMapper.toEntity(inputDTO, business)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(savedDepartment);
        when(departmentMapper.toDto(savedDepartment)).thenReturn(outputDTO);

        // Act
        DepartmentOutputDTO result = departmentService.createDepartment(inputDTO);

        // Assert
        assertEquals(outputDTO, result);
    }

    @Test
    void testPatchDepartment() {
        // Arrange
        Long departmentId = 1L;
        DepartmentInputDTO inputDTO = new DepartmentInputDTO();
        Department department = new Department();
        Department updatedDepartment = new Department();
        DepartmentOutputDTO outputDTO = new DepartmentOutputDTO();

        inputDTO.setDepartmentName("Updated Name");
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(departmentRepository.save(department)).thenReturn(updatedDepartment);
        when(departmentMapper.toDto(updatedDepartment)).thenReturn(outputDTO);

        // Act
        DepartmentOutputDTO result = departmentService.patchDepartment(departmentId, inputDTO);

        // Assert
        assertEquals(outputDTO, result);
        verify(departmentRepository).save(department);
    }

    @Test
    void testFindDepartmentByIdSuccess() {
        // Arrange
        Long departmentId = 1L;
        Department department = new Department();
        DepartmentOutputDTO outputDTO = new DepartmentOutputDTO();
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(departmentMapper.toDto(department)).thenReturn(outputDTO);

        // Act
        Optional<DepartmentOutputDTO> result = departmentService.findDepartmentById(departmentId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(outputDTO, result.get());
    }

    @Test
    void testFindDepartmentByIdNotFound() {
        // Arrange
        Long departmentId = 1L;
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        // Act
        Optional<DepartmentOutputDTO> result = departmentService.findDepartmentById(departmentId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteDepartmentById() {
        // Arrange
        Long departmentId = 1L;
        when(departmentRepository.existsById(departmentId)).thenReturn(true);

        // Act
        departmentService.deleteDepartmentById(departmentId);

        // Assert
        verify(departmentRepository, times(1)).deleteById(departmentId);
    }

    @Test
    void testDeleteDepartmentByIdNotFound() {
        // Arrange
        Long departmentId = 1L;
        when(departmentRepository.existsById(departmentId)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> departmentService.deleteDepartmentById(departmentId));
    }
}