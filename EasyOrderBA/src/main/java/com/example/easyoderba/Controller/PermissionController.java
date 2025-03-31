package com.example.easyoderba.Controller;


import com.example.easyoderba.Model.DTO.request.AuthReq.PermissionReq;
import com.example.easyoderba.Model.DTO.response.ApiResponse;
import com.example.easyoderba.Model.DTO.response.PermissionResponse;
import com.example.easyoderba.Service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionReq permissionReq) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.createPermission(permissionReq))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAllPermissions() {
        return  ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAllPermissions())
                .build();
    }
    @DeleteMapping("/{id}")
    ApiResponse<String> deletePermissionsByPermission(@PathVariable Long id) {
        return ApiResponse.<String>builder()
                .result(permissionService.deletePermissionsById(id))
                .build();
    }
}
