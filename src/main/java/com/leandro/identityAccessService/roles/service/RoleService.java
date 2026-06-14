package com.leandro.identityAccessService.roles.service;

import com.leandro.identityAccessService.roles.dto.RoleCreateDTO;
import com.leandro.identityAccessService.roles.dto.RoleDTO;
import com.leandro.identityAccessService.roles.dto.RoleUpdateDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface RoleService {
    RoleDTO create(@Valid RoleCreateDTO roleCreateDTO);

    List<RoleDTO> getAll();

    RoleDTO getById(String id);

    void delete(String id);

    RoleDTO update(String id, RoleUpdateDTO roleUpdateDTO);

    RoleDTO addPermission(String id, String permissionId);

    RoleDTO removePermission(String id, String permissionId);

    List<RoleDTO> getAllByIds(List<String> rolesIds);

    void verifyRoleId(String roleId);
}
