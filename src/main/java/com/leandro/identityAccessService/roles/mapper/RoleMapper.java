package com.leandro.identityAccessService.roles.mapper;

import com.leandro.identityAccessService.common.util.UpdateUtils;
import com.leandro.identityAccessService.permission.dto.PermissionDTO;
import com.leandro.identityAccessService.permission.model.Permission;
import com.leandro.identityAccessService.roles.dto.RoleCreateDTO;
import com.leandro.identityAccessService.roles.dto.RoleDTO;
import com.leandro.identityAccessService.roles.dto.RoleUpdateDTO;
import com.leandro.identityAccessService.roles.model.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final ModelMapper modelMapper;


    public Role toEntity(RoleCreateDTO roleCreateDTO) {
        Role role = modelMapper.map(roleCreateDTO, Role.class);
        role.setCreatedAt(Instant.now());
        return role;
    }

    public RoleDTO toDTO(Role role, List<PermissionDTO> permissions) {
        RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);
        roleDTO.setPermissions(permissions);
        return roleDTO;
    }

    public List<RoleDTO> toDTOs(List<Role> roles, List<PermissionDTO> permissions) {
        return roles.stream().map(role -> {
            List<PermissionDTO> rolePermissions = permissions.stream()
                    .filter(permission -> role.getPermissionIds() != null && role.getPermissionIds().contains(permission.getId()))
                    .toList();
            return toDTO(role, rolePermissions);
        }).toList();
    }

    public void updateEntity(Role role, RoleUpdateDTO roleUpdateDTO) {
        UpdateUtils.setIfNotNull(roleUpdateDTO.getName(), role::setName);
        role.setUpdatedAt(Instant.now());
    }
}
