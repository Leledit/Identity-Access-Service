package com.leandro.identityAccessService.user.service.impl;

import com.leandro.identityAccessService.common.exception.BadRequestException;
import com.leandro.identityAccessService.common.exception.ErrorCode;
import com.leandro.identityAccessService.roles.dto.RoleDTO;
import com.leandro.identityAccessService.roles.model.Role;
import com.leandro.identityAccessService.roles.service.RoleService;
import com.leandro.identityAccessService.user.dto.UserAuthDTO;
import com.leandro.identityAccessService.user.dto.UserCreateDTO;
import com.leandro.identityAccessService.user.dto.UserDTO;
import com.leandro.identityAccessService.user.dto.UserUpdateDTO;
import com.leandro.identityAccessService.user.mapper.UserMapper;
import com.leandro.identityAccessService.user.model.User;
import com.leandro.identityAccessService.user.repository.UserRepository;
import com.leandro.identityAccessService.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO create(UserCreateDTO userCreateDTO) {
        Optional<User> optionalUser = userRepository.findByEmailAndDeletedAtIsNull(userCreateDTO.getEmail());

        if (optionalUser.isPresent()) {
            throw new BadRequestException(ErrorCode.EMAIL_ALREADY_EXISTS, "Failed to create user: Email already exists");
        }

        User user = userMapper.toEntity(userCreateDTO);
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));

        List<RoleDTO> roleDTOS = getRoleDTOS(userCreateDTO);

        userRepository.save(user);
        return userMapper.toDTO(user, roleDTOS);
    }

    private @NonNull List<RoleDTO> getRoleDTOS(UserCreateDTO userCreateDTO) {
        List<RoleDTO> roleDTOS = new ArrayList<>();
        try {
            userCreateDTO.getRoleIds().forEach(roleId -> {
                RoleDTO roleDTO = roleService.getById(roleId);
                roleDTOS.add(roleDTO);
            });
        }catch (BadRequestException e) {
            throw new BadRequestException(ErrorCode.ROLE_NOT_FOUND, "Failed to create user: " + e.getMessage());
        }
        return roleDTOS;
    }

    @Override
    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAllByDeletedAtIsNull();

        Set<String> roleIds = new HashSet<>();

        users.forEach(user -> {
            if(!user.getRoleIds().isEmpty()) {
                roleIds.addAll(user.getRoleIds());
            }
        });

        List<RoleDTO> roleDTOS = roleService.getAllByIds(new ArrayList<>(roleIds));

        return userMapper.toDOS(users, roleDTOS);
    }

    @Override
    public UserDTO getById(String userId) {
        User user = findById(userId);

        return getUserDTO(user);
    }

    private UserDTO getUserDTO(User user) {
        List<RoleDTO> roleDTOS = new ArrayList<>();
        if(!user.getRoleIds().isEmpty()) {
            roleDTOS = roleService.getAllByIds(user.getRoleIds());
        }

        return userMapper.toDTO(user, roleDTOS);
    }

    @Override
    public void delete(String userId) {
        User user = findById(userId);
        user.setDeletedAt(Instant.now());

        userRepository.save(user);
    }

    @Override
    public UserDTO addRole(String userId, String roleId) {
        User user = findById(userId);

        if (user.getRoleIds().contains(roleId)) {
            throw new BadRequestException(
                    ErrorCode.ROLE_ALREADY_EXISTS,
                    "Role with id '" + roleId + "' already exists in user"
            );
        }

        roleService.verifyRoleId(roleId);
        user.setRoleIds(new ArrayList<>(user.getRoleIds()) {{add(roleId);}});
        user.setUpdatedAt(Instant.now());

        userRepository.save(user);

        return getUserDTO(user);
    }

    @Override
    public UserDTO removeRole(String userId, String roleId) {
        User user = findById(userId);

        roleService.verifyRoleId(roleId);

        user.getRoleIds().stream()
                .filter(r -> r.equals(roleId))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorCode.ROLE_NOT_FOUND, "Failed to remove role: Role with id '" + roleId + "' not found in user with id '" + userId + "'"));

        user.getRoleIds().remove(roleId);
        user.setUpdatedAt(Instant.now());

        userRepository.save(user);
        return getUserDTO(user);
    }


    @Override
    public UserDTO update(String userId, UserUpdateDTO userUpdateDTO) {
        User user = findById(userId);

        userMapper.updateEntity(user, userUpdateDTO);

        if (userUpdateDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }

        userRepository.save(user);
        return getUserDTO(user);
    }

    @Override
    public UserAuthDTO getUserAuthInfo(String userId) {
        User user = findById(userId);

        Set<String> roleIds = new HashSet<>();

        user.getRoleIds().forEach(roleId -> {
            RoleDTO role = roleService.getById(roleId);

            role.getPermissions().forEach(permission -> {
                roleIds.add(permission.getName());
            });


        });

        return userMapper.toAuthDTO(user, roleIds.stream().toList());
    }

    private User findById(String userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND, "User with id '" + userId + "' not found"));
    }
}
