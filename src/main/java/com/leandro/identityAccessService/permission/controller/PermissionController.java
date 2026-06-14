package com.leandro.identityAccessService.permission.controller;

import com.leandro.identityAccessService.permission.dto.PermissionCreateDTO;
import com.leandro.identityAccessService.permission.dto.PermissionDTO;
import com.leandro.identityAccessService.permission.dto.PermissionUpdateDTO;
import com.leandro.identityAccessService.permission.service.PermissionService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/permission")
public class PermissionController {

    private final PermissionService permissionService;

    @PreAuthorize("hasAuthority('PERMISSION_CREATE')")
    @PostMapping(produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PermissionDTO> createPermission (@RequestBody @Valid PermissionCreateDTO permissionCreateDTO) {
        PermissionDTO permissionDTO = permissionService.create(permissionCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionDTO);
    }

    @PreAuthorize("hasAuthority('PERMISSION_READ_ALL')")
    @GetMapping(produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        List<PermissionDTO> permissionDTOS = permissionService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(permissionDTOS);
    }

    @PreAuthorize("hasAuthority('PERMISSION_READ')")
    @GetMapping(value = "/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PermissionDTO> getPermissionById (@PathVariable String id) {
        PermissionDTO permissionDTO = permissionService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(permissionDTO);
    }

    @PreAuthorize("hasAuthority('PERMISSION_DELETE')")
    @DeleteMapping(value = "/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deletePermission (@PathVariable String id) {
        permissionService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasAuthority('PERMISSION_UPDATE')")
    @PatchMapping(value = "/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PermissionDTO> updatePermission (@PathVariable String id,
                                                           @RequestBody PermissionUpdateDTO permissionUpdateDTO) {

        PermissionDTO updatedPermission = permissionService.update(id, permissionUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPermission);

    }
}
