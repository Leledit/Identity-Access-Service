package com.leandro.identityAccessService.permission.repository;

import com.leandro.identityAccessService.permission.model.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends MongoRepository<Permission, String> {

    List<Permission> findAllByDeletedAtIsNull();

    List<Permission> findAllByIdInAndDeletedAtIsNull(List<String> ids);

    Optional<Permission> findByIdAndDeletedAtIsNull(String id);
}
