package com.SupportHub.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SupportHub.demo.dtos.InputDTOs.AdminInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.AdminOutputDTO;
import com.SupportHub.demo.services.AdminService;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // GET /api/admins/{adminId}
    @GetMapping("/{adminId}")
    public ResponseEntity<AdminOutputDTO> getAdminById(@PathVariable Long adminId) {
        return adminService.findAdminById(adminId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET /api/admins/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<AdminOutputDTO> getAdminByUserId(@PathVariable Long userId) {
        return adminService.findAdminByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET /api/admins
    @GetMapping
    public ResponseEntity<List<AdminOutputDTO>> getAllAdmins() {
        List<AdminOutputDTO> admins = adminService.findAllAdmins();
        return ResponseEntity.ok(admins);
    }

    // POST /api/admins
    @PostMapping
    public ResponseEntity<AdminOutputDTO> createAdmin(@RequestBody AdminInputDTO adminInputDTO) {
        try {
            AdminOutputDTO createdAdmin = adminService.createAdmin(adminInputDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}