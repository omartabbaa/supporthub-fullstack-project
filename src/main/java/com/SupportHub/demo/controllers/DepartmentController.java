package com.SupportHub.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SupportHub.demo.dtos.InputDTOs.DepartmentInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.DepartmentOutputDTO;
import com.SupportHub.demo.DepartmentService;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // GET /api/departments/{departmentId}
    @GetMapping("/{departmentId}")
    public ResponseEntity<DepartmentOutputDTO> getDepartmentById(@PathVariable Long departmentId) {
        Optional<DepartmentOutputDTO> department = departmentService.findDepartmentById(departmentId);
        return department.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // GET /api/departments
    @GetMapping
    public ResponseEntity<List<DepartmentOutputDTO>> getAllDepartments() {
        List<DepartmentOutputDTO> departments = departmentService.findAllDepartments();
        return ResponseEntity.ok(departments);
    }

    // POST /api/departments
    @PostMapping
    public ResponseEntity<DepartmentOutputDTO> createDepartment(@RequestBody DepartmentInputDTO departmentInputDTO) {
        DepartmentOutputDTO createdDepartment = departmentService.createDepartment(departmentInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }

    // PUT /api/departments/{departmentId}
    @PutMapping("/{departmentId}")
    public ResponseEntity<DepartmentOutputDTO> updateDepartment(@PathVariable Long departmentId,
                                                                @RequestBody DepartmentInputDTO departmentInputDTO) {
        try {
            DepartmentOutputDTO updatedDepartment = departmentService.updateDepartment(departmentId, departmentInputDTO);
            return ResponseEntity.ok(updatedDepartment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE /api/departments/{departmentId}
    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long departmentId) {
        try {
            departmentService.deleteDepartmentById(departmentId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/{departmentId}")
    public ResponseEntity<DepartmentOutputDTO> patchDepartment(@PathVariable Long departmentId,
                                                                @RequestBody DepartmentInputDTO departmentInputDTO) {
        DepartmentOutputDTO updatedDepartment = departmentService.patchDepartment(departmentId, departmentInputDTO);
        return ResponseEntity.ok(updatedDepartment);
    }
}
