package com.leandro.identityAccessService.permission.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PermissionCreateDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}
