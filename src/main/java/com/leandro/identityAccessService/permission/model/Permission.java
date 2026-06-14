package com.leandro.identityAccessService.permission.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document("permission")
public class Permission {

    @Id
    private String id;

    private String name;
    private String description;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
