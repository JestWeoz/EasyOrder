package com.example.easyoderba.Service.AuthService.Implements;

import com.example.easyoderba.Model.DTO.request.AuthReq.PermissionReq;
import com.example.easyoderba.Model.DTO.response.AuthRes.PermissionResponse;
import com.example.easyoderba.Model.Entity.AuthEntity.Permission;
import com.example.easyoderba.Repository.AuthRespository.PermissionRepository;
import com.example.easyoderba.Service.AuthService.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImp implements PermissionService {
    PermissionRepository permissionRepository;
    ModelMapper modelMapper;


    @Override
    public PermissionResponse createPermission(PermissionReq req) {
        Permission permission;
        permission = modelMapper.map(req, Permission.class);
        permission = permissionRepository.save(permission);
        return modelMapper.map(req, PermissionResponse.class);
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        List<PermissionResponse> permissionResponses = new ArrayList<>();
        permissions.forEach(permission -> permissionResponses.add(modelMapper.map(permission, PermissionResponse.class)));
        return permissionResponses;
    }

    @Override
    public String deletePermissionsById(Long id) {
        permissionRepository.deleteById(id);
        return "Deleted Permission";
    }




}
