package com.leandro.identityAccessService.permission.mapper;

import com.leandro.identityAccessService.common.util.UpdateUtils;
import com.leandro.identityAccessService.permission.dto.PermissionCreateDTO;
import com.leandro.identityAccessService.permission.dto.PermissionDTO;
import com.leandro.identityAccessService.permission.dto.PermissionUpdateDTO;
import com.leandro.identityAccessService.permission.model.Permission;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PermissionMapper {

    private final ModelMapper modelMapper;

    public Permission toEntity(PermissionCreateDTO permissionCreateDTO) {
        Permission permission = modelMapper.map(permissionCreateDTO, Permission.class);
        permission.setCreatedAt(Instant.now());
        return permission;
    }

    public void updateEntity(PermissionUpdateDTO permissionUpdateDTO, Permission permission) {
        UpdateUtils.setIfNotNull(permissionUpdateDTO.getName(), permission::setName);
        UpdateUtils.setIfNotNull(permissionUpdateDTO.getDescription(), permission::setDescription);
        permission.setUpdatedAt(Instant.now());
    }

    public PermissionDTO toDto(Permission permission) {
        return modelMapper.map(permission, PermissionDTO.class);
    }

    public List<PermissionDTO> toDtos(List<Permission> permissions) {
        return permissions.stream().map(this::toDto).toList();
    }
}