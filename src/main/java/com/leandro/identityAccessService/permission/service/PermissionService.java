package com.leandro.identityAccessService.permission.service;

import com.leandro.identityAccessService.permission.dto.PermissionCreateDTO;
import com.leandro.identityAccessService.permission.dto.PermissionDTO;
import com.leandro.identityAccessService.permission.dto.PermissionUpdateDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface PermissionService {
    PermissionDTO create(@Valid PermissionCreateDTO permissionCreateDTO);

    List<PermissionDTO> getAll();

    PermissionDTO getById(String permissionId);

    void delete(String permissionId);

    PermissionDTO update(String permissionId, PermissionUpdateDTO permissionUpdateDTO);

    List<PermissionDTO> getAllByIds(List<String> permissionIds);

    void verifyPermissionId(String permissionId);
}
