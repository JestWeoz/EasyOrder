package com.example.easyoderba.Controller.AuthController;

import com.example.easyoderba.Model.DTO.request.AuthReq.RoleReq;
import com.example.easyoderba.Model.DTO.response.AuthRes.ApiResponse;
import com.example.easyoderba.Model.DTO.response.AuthRes.RoleResponse;
import com.example.easyoderba.Service.AuthService.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleReq roleReq) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.createRole(roleReq))
                .build();
    }
    @GetMapping
    public ApiResponse<List<RoleResponse>> getRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAllRoles())
                .build();
    }
}
