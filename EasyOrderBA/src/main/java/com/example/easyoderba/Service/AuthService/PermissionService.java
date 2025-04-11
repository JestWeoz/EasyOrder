package com.example.easyoderba.Service.AuthService;

import com.example.easyoderba.Model.DTO.request.AuthReq.PermissionReq;
import com.example.easyoderba.Model.DTO.response.AuthRes.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse createPermission(PermissionReq req);
    List<PermissionResponse> getAllPermissions();
    String deletePermissionsById(Long id);
}
