package com.leandro.identityAccessService.user.service;

import com.leandro.identityAccessService.user.dto.UserAuthDTO;
import com.leandro.identityAccessService.user.dto.UserCreateDTO;
import com.leandro.identityAccessService.user.dto.UserDTO;
import com.leandro.identityAccessService.user.dto.UserUpdateDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {
    UserDTO create( UserCreateDTO userCreateDTO);

    List<UserDTO> getAll();

    UserDTO getById(String userId);

    void delete(String userId);

    UserDTO addRole(String userId, String roleId);

    UserDTO removeRole(String userId, String roleId);

    UserDTO update(String userId, UserUpdateDTO userUpdateDTO);

    UserAuthDTO getUserAuthInfo(String userId);
}
