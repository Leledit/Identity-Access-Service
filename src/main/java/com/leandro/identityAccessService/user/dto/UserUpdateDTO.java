package com.leandro.identityAccessService.user.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String username;
    private String password;
    private String email;
    private Boolean active;
}
