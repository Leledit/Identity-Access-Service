package com.leandro.identityAccessService.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UserCreateDTO {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    @NotNull
    private Boolean active;

    private List<String> roleIds;
}
