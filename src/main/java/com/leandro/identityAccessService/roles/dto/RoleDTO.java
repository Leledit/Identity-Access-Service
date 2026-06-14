package com.leandro.identityAccessService.roles.dto;

import com.leandro.identityAccessService.permission.dto.PermissionDTO;
import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {
    private String id;
    private String name;

    private List<PermissionDTO> permissions;
}
