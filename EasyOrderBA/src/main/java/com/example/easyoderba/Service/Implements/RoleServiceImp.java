package com.example.easyoderba.Service.Implements;

import com.example.easyoderba.Model.DTO.request.RoleReq;
import com.example.easyoderba.Model.DTO.response.RoleResponse;
import com.example.easyoderba.Model.Entity.AuthEntity.Permission;
import com.example.easyoderba.Model.Entity.AuthEntity.Role;
import com.example.easyoderba.Repository.PermissionRepository;
import com.example.easyoderba.Repository.RoleRepository;
import com.example.easyoderba.Service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImp implements RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    ModelMapper modelMapper;


    @Override
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
