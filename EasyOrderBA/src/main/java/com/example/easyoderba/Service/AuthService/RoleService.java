package com.example.easyoderba.Service.AuthService;

import com.example.easyoderba.Model.DTO.request.AuthReq.RoleReq;
import com.example.easyoderba.Model.DTO.response.AuthRes.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(RoleReq req);
    List<RoleResponse> getAllRoles();
}
