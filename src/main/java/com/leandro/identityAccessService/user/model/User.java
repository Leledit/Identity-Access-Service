package com.leandro.identityAccessService.user.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Document("user")
public class User {

    @Id
    private String id;

    private String username;
    private String email;
    private String password;
    private Boolean active;

    private List<String> roleIds;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
