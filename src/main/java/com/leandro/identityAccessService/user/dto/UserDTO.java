package com.leandro.identityAccessService.user.dto;

import com.leandro.identityAccessService.roles.dto.RoleDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String id;

    private String username;
    private String email;
    private Boolean active;

    private List<RoleDTO> roles;
}