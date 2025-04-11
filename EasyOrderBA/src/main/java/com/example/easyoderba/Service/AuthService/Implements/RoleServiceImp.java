package com.example.easyoderba.Service.AuthService.Implements;

import com.example.easyoderba.Model.DTO.request.AuthReq.RoleReq;
import com.example.easyoderba.Model.DTO.response.AuthRes.RoleResponse;
import com.example.easyoderba.Model.Entity.AuthEntity.Permission;
import com.example.easyoderba.Model.Entity.AuthEntity.Role;
import com.example.easyoderba.Repository.AuthRespository.PermissionRepository;
import com.example.easyoderba.Repository.AuthRespository.RoleRepository;
import com.example.easyoderba.Service.AuthService.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImp implements RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    ModelMapper modelMapper;


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse createRole(RoleReq req) {
        Role role = modelMapper.map(req, Role.class);
        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(req.getPermissions()));
        role.setPermissions(permissions);
        roleRepository.save(role);
        return modelMapper.map(role, RoleResponse.class);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        List<RoleResponse> roleResponses = new ArrayList<>();
        roles.forEach(role -> roleResponses.add(modelMapper.map(role, RoleResponse.class)));
        return roleResponses;
    }
}
