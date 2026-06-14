package com.leandro.identityAccessService.user.controller;

import com.leandro.identityAccessService.user.dto.UserCreateDTO;
import com.leandro.identityAccessService.user.dto.UserDTO;
import com.leandro.identityAccessService.user.dto.UserUpdateDTO;
import com.leandro.identityAccessService.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('USER_CREATE')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        UserDTO userDTO = userService.create(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PreAuthorize("hasAuthority('USER_READ_ALL')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id,
                                              @RequestBody UserUpdateDTO userUpdateDTO) {

        UserDTO userDTO = userService.update(id, userUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        UserDTO userDTO = userService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PreAuthorize("hasAuthority('USER_DELETE')")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasAuthority('USER_ADD_ROLE')")
    @PostMapping(value = "/{id}/add-role", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> addRole(@PathVariable String id, @RequestParam String roleId) {
        UserDTO userDTO = userService.addRole(id,roleId);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PreAuthorize("hasAuthority('USER_DELETE_ROLE')")
    @DeleteMapping(value = "/{id}/remove-role", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> removeRole(@PathVariable String id, @RequestParam String roleId) {
        UserDTO userDTO =  userService.removeRole(id, roleId);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    //TODO: Considerar se deve ser feita uma rota apenas para atualizar a senha
    //TODO: Criar rota de login.
    //TODO: Testar a autenticação
    //cadadastrar as novas permições na role do user (admin)

}
