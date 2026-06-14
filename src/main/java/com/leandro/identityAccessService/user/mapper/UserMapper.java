package com.leandro.identityAccessService.user.mapper;

import com.leandro.identityAccessService.common.util.UpdateUtils;
import com.leandro.identityAccessService.roles.dto.RoleDTO;
import com.leandro.identityAccessService.user.dto.UserAuthDTO;
import com.leandro.identityAccessService.user.dto.UserCreateDTO;
import com.leandro.identityAccessService.user.dto.UserDTO;
import com.leandro.identityAccessService.user.dto.UserUpdateDTO;
import com.leandro.identityAccessService.user.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public User toEntity(UserCreateDTO userCreateDTO) {
        User user = modelMapper.map(userCreateDTO, User.class);
        user.setCreatedAt(Instant.now());
        return user;
    }

    public UserDTO toDTO(User user, List<RoleDTO> roles) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setRoles(roles);
        return userDTO;
    }

    public UserAuthDTO toAuthDTO(User user, List<String> permissions) {
        UserAuthDTO userAuthDTO = modelMapper.map(user, UserAuthDTO.class);
        userAuthDTO.setPermissions(permissions);
        return userAuthDTO;
    }

    public List<UserDTO> toDOS(List<User> users, List<RoleDTO> roleDTOS) {
        return users.stream().map(user -> {
            List<RoleDTO> userRoles = roleDTOS.stream()
                    .filter(role -> user.getRoleIds() != null && user.getRoleIds().contains(role.getId()))
                    .toList();
            return toDTO(user, userRoles);
        }).toList();
    }

    public void updateEntity(User user, UserUpdateDTO userUpdateDTO) {
        UpdateUtils.setIfNotNull(userUpdateDTO.getEmail(), user::setEmail);
        UpdateUtils.setIfNotNull(userUpdateDTO.getUsername(), user::setUsername);
        UpdateUtils.setIfNotNull(userUpdateDTO.getActive(), user::setActive);
        user.setUpdatedAt(Instant.now());
    }
}
