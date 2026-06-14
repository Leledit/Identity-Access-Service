package com.leandro.identityAccessService.roles.repository;

import com.leandro.identityAccessService.roles.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByName(String name);

    List<Role> findAllByDeletedAtIsNull();

    Optional<Role> findByIdAndDeletedAtIsNull(String roleId);

    List<Role> findAllByIdInAndDeletedAtIsNull(List<String> roleIds);
}
