package com.example.easyoderba.Service;

import com.example.easyoderba.Model.DTO.request.PermissionReq;
import com.example.easyoderba.Model.DTO.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse createPermission(PermissionReq req);
    List<PermissionResponse> getAllPermissions();
    String deletePermissionsById(Long id);
}
