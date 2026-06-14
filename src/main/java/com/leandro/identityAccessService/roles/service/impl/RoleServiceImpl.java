package com.leandro.identityAccessService.roles.service.impl;

import com.leandro.identityAccessService.common.exception.BadRequestException;
import com.leandro.identityAccessService.common.exception.ErrorCode;
import com.leandro.identityAccessService.permission.dto.PermissionDTO;
import com.leandro.identityAccessService.permission.service.PermissionService;
import com.leandro.identityAccessService.roles.dto.RoleCreateDTO;
import com.leandro.identityAccessService.roles.dto.RoleDTO;
import com.leandro.identityAccessService.roles.dto.RoleUpdateDTO;
import com.leandro.identityAccessService.roles.mapper.RoleMapper;
import com.leandro.identityAccessService.roles.model.Role;
import com.leandro.identityAccessService.roles.repository.RoleRepository;
import com.leandro.identityAccessService.roles.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    @Override
    public RoleDTO create(RoleCreateDTO roleCreateDTO) {
        Role roleFound = roleRepository.findByName(roleCreateDTO.getName()).orElse(null);

        if (roleFound != null) {
            throw new BadRequestException(ErrorCode.ROLE_ALREADY_EXISTS, "Role with name '" + roleCreateDTO.getName() + "' already exists");
        }

        Role role = roleMapper.toEntity(roleCreateDTO);

        List<PermissionDTO> permissions = getPermissionDTOS(role);

        roleRepository.save(role);
        return roleMapper.toDTO(role, permissions);
    }

    @Override
    public List<RoleDTO> getAll() {
        List<Role> roles = roleRepository.findAllByDeletedAtIsNull();

        List<PermissionDTO> permissionDTOS = getPermissionDTOS(roles);
        return roleMapper.toDTOs(roles, permissionDTOS);
    }

    private List<PermissionDTO> getPermissionDTOS(List<Role> roles) {
        Set<String> permissionIds = new HashSet<>();

        roles.forEach(role -> {
            if (role.getPermissionIds() != null) {
                permissionIds.addAll(role.getPermissionIds());
            }
        });

        List<PermissionDTO> permissionDTOS = permissionService.getAllByIds(new ArrayList<>(permissionIds));
        return permissionDTOS;
    }

    @Override
    public RoleDTO getById(String roleId) {
        Role role = findById(roleId);
        List<PermissionDTO> permissions = getPermissionDTOS(role);

        return roleMapper.toDTO(role, permissions);
    }

    @Override
    public void delete(String roleId) {
        Role role = findById(roleId);

        role.setDeletedAt(Instant.now());
        roleRepository.save(role);
    }

    @Override
    public RoleDTO update(String roleId, RoleUpdateDTO roleUpdateDTO) {
        Role role = findById(roleId);

        roleMapper.updateEntity(role, roleUpdateDTO);
        roleRepository.save(role);

        List<PermissionDTO> permissions = getPermissionDTOS(role);
        return roleMapper.toDTO(role, permissions);
    }

    @Override
    public RoleDTO addPermission(String roleId, String permissionId) {
        Role role = findById(roleId);

        permissionService.verifyPermissionId(permissionId);

        if (role.getPermissionIds().contains(permissionId)) {
            throw new BadRequestException(ErrorCode.PERMISSION_ALREADY_EXISTS, "Permission with id '" + permissionId + "' already exists in role");
        }

        role.getPermissionIds().add(permissionId);
        role.setPermissionIds(role.getPermissionIds());
        role.setUpdatedAt(Instant.now());

        roleRepository.save(role);

        List<PermissionDTO> permissions = getPermissionDTOS(role);
        return roleMapper.toDTO(role, permissions);
    }

    @Override
    public RoleDTO removePermission(String roleId, String permissionId) {
        Role role = findById(roleId);

        permissionService.verifyPermissionId(permissionId);

        role.getPermissionIds().stream().filter(permission -> permission.equals(permissionId)).findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorCode.PERMISSION_NOT_FOUND, "Permission with id '" + permissionId + "' not found in role"));


        role.getPermissionIds().remove(permissionId);
        role.setUpdatedAt(Instant.now());
        roleRepository.save(role);

        List<PermissionDTO> permissions = getPermissionDTOS(role);
        return roleMapper.toDTO(role, permissions);
    }

    @Override
    public List<RoleDTO> getAllByIds(List<String> rolesIds) {
        List<Role> roles = roleRepository.findAllByIdInAndDeletedAtIsNull(rolesIds);
        List<PermissionDTO> permissionDTOS = getPermissionDTOS(roles);
        return roleMapper.toDTOs(roles, permissionDTOS);
    }

    @Override
    public void verifyRoleId(String roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new BadRequestException(ErrorCode.ROLE_NOT_FOUND, "Role with id '" + roleId + "' not found");
        }
    }

    private Role findById(String roleId) {
        return roleRepository.findByIdAndDeletedAtIsNull(roleId)
                .orElseThrow(()-> new BadRequestException(ErrorCode.ROLE_NOT_FOUND, "Role with id '" + roleId + "' not found"));
    }

    private @NonNull List<PermissionDTO> getPermissionDTOS(Role role) {
        List<PermissionDTO> permissions = new ArrayList<>();
        try {
            role.getPermissionIds().forEach((permissionId) -> {
                PermissionDTO permission = permissionService.getById(permissionId);
                permissions.add(permission);
            });
        } catch (BadRequestException e) {
            throw new BadRequestException(ErrorCode.PERMISSION_NOT_FOUND, "One or more permissions not found");
        }
        return permissions;
    }
}
