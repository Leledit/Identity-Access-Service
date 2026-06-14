package com.leandro.identityAccessService.roles.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Document("role")
public class Role {

    @Id
    private String id;

    private String name;

    private List<String> permissionIds;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}