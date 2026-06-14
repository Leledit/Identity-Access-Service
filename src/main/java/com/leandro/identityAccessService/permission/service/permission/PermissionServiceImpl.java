package com.leandro.identityAccessService.permission.service.permission;

import com.leandro.identityAccessService.common.exception.BadRequestException;
import com.leandro.identityAccessService.common.exception.ErrorCode;
import com.leandro.identityAccessService.permission.dto.PermissionCreateDTO;
import com.leandro.identityAccessService.permission.dto.PermissionDTO;
import com.leandro.identityAccessService.permission.dto.PermissionUpdateDTO;
import com.leandro.identityAccessService.permission.mapper.PermissionMapper;
import com.leandro.identityAccessService.permission.model.Permission;
import com.leandro.identityAccessService.permission.repository.PermissionRepository;
import com.leandro.identityAccessService.permission.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;
    private final PermissionRepository permissionRepository;

    @Override
    public PermissionDTO create(PermissionCreateDTO permissionCreateDTO) {
        Permission permission = permissionMapper.toEntity(permissionCreateDTO);
        permission = permissionRepository.save(permission);
        return permissionMapper.toDto(permission);
    }

    @Override
    public List<PermissionDTO> getAll() {
        List<Permission> permissions = permissionRepository.findAllByDeletedAtIsNull();
        return permissionMapper.toDtos(permissions);
    }

    @Override
    public PermissionDTO getById(String permissionId) {
        Permission permission = getPermissionById(permissionId);
        return permissionMapper.toDto(permission);
    }

    @Override
    public void delete(String permissionId) {
        Permission permission = getPermissionById(permissionId);
        permission.setDeletedAt(Instant.now());
        permissionRepository.save(permission);
    }

    @Override
    public PermissionDTO update(String permissionId, PermissionUpdateDTO permissionUpdateDTO) {
        Permission permission = getPermissionById(permissionId);

        permissionMapper.updateEntity(permissionUpdateDTO, permission);
        permission = permissionRepository.save(permission);

        return permissionMapper.toDto(permission);
    }

    @Override
    public List<PermissionDTO> getAllByIds(List<String> permissionIds) {
        List<Permission> permissions = permissionRepository.findAllByIdInAndDeletedAtIsNull(permissionIds);
        return permissionMapper.toDtos(permissions);
    }

    @Override
    public void verifyPermissionId(String permissionId) {
        boolean exists = permissionRepository.existsById(permissionId);

        if (!exists) {
            throw new BadRequestException(ErrorCode.PERMISSION_NOT_FOUND, "Permission with id '" + permissionId + "' not found");
        }
    }

    private Permission getPermissionById(String id) {
        return permissionRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new BadRequestException(ErrorCode.PERMISSION_NOT_FOUND));
    }
}