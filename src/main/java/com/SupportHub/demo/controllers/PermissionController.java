package com.SupportHub.demo.controllers;

import java.util.List;

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

import com.SupportHub.demo.dtos.InputDTOs.PermissionInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.PermissionsOutputDTO;
import com.SupportHub.demo.PermissionsService;


@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionsService permissionsService;

    @Autowired
    public PermissionController(PermissionsService permissionsService) {
        this.permissionsService = permissionsService;
    }

    // GET /api/permissions/{permissionId}
    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionsOutputDTO> getPermissionById(@PathVariable Long permissionId) {
        PermissionsOutputDTO permission = permissionsService.getPermissionById(permissionId);
        return permission != null
                ? ResponseEntity.ok(permission)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // GET /api/permissions
    @GetMapping
    public ResponseEntity<List<PermissionsOutputDTO>> getAllPermissions() {
        List<PermissionsOutputDTO> permissions = permissionsService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    // POST /api/permissions
    @PostMapping
    public ResponseEntity<PermissionsOutputDTO> createPermission(@RequestBody PermissionInputDTO permissionsInputDTO) {
        PermissionsOutputDTO createdPermission = permissionsService.createPermission(permissionsInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPermission);
    }

    // PUT /api/permissions/{permissionId}
    @PutMapping("/{permissionId}")
    public ResponseEntity<PermissionsOutputDTO> updatePermission(@PathVariable Long permissionId,
                                                                @RequestBody PermissionInputDTO permissionsInputDTO) {
        try {
            PermissionsOutputDTO updatedPermission = permissionsService.updatePermission(permissionId, permissionsInputDTO);
            return ResponseEntity.ok(updatedPermission);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/{permissionId}")
    public ResponseEntity<PermissionsOutputDTO> patchPermission(@PathVariable Long permissionId,
                                                                @RequestBody PermissionInputDTO permissionsInputDTO) {
        PermissionsOutputDTO updatedPermission = permissionsService.patchPermission(permissionId, permissionsInputDTO);
        return ResponseEntity.ok(updatedPermission);
    }



    // DELETE /api/permissions/{permissionId}
    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long permissionId) {
        try {
            permissionsService.deletePermission(permissionId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
