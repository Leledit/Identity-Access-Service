package com.leandro.identityAccessService.roles.controller;

import com.leandro.identityAccessService.roles.dto.RoleCreateDTO;
import com.leandro.identityAccessService.roles.dto.RoleDTO;
import com.leandro.identityAccessService.roles.dto.RoleUpdateDTO;
import com.leandro.identityAccessService.roles.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/role")
public class RoleController {

    private final RoleService roleService;

    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> createRole(@RequestBody @Valid RoleCreateDTO roleCreateDTO) {
        RoleDTO roleDTO = roleService.create(roleCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleDTO);
    }

    @PreAuthorize("hasAuthority('ROLE_READ_ALL')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roleDTOS = roleService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(roleDTOS);
    }

    @PreAuthorize("hasAuthority('ROLE_READ')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable String id) {
        RoleDTO roleDTO = roleService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(roleDTO);
    }

    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteRoleById(@PathVariable String id) {
        roleService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> updateRoleById(@PathVariable String id, @RequestBody RoleUpdateDTO roleUpdateDTO) {
        RoleDTO updatedRole = roleService.update(id, roleUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRole);
    }

    @PreAuthorize("hasAuthority('ROLE_ADD_PERMISION')")
    @PostMapping(value = "/{id}/add-permission", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> addPermission(@PathVariable String id, @RequestParam String permissionId) {
        RoleDTO updatedRole = roleService.addPermission(id, permissionId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRole);
    }

    @PreAuthorize("hasAuthority('ROLE_DELETE_PERMISION')")
    @DeleteMapping(value = "/{id}/remove-permission", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> removePermission(@PathVariable String id, @RequestParam String permissionId) {
        RoleDTO updatedRole = roleService.removePermission(id, permissionId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRole);
    }
}