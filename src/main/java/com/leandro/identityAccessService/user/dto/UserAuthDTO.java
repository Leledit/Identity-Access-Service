package com.leandro.identityAccessService.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserAuthDTO {
    private String id;

    private String username;
    private String email;
    private Boolean active;

    List<String> permissions;
}
