package com.leandro.identityAccessService.user.repository;

import com.leandro.identityAccessService.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmailAndDeletedAtIsNull(String email);

    List<User> findAllByDeletedAtIsNull();


    Optional<User> findByIdAndDeletedAtIsNull(String id);
}
