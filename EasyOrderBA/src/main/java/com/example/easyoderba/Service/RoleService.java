package com.example.easyoderba.Service;

import com.example.easyoderba.Model.DTO.request.RoleReq;
import com.example.easyoderba.Model.DTO.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(RoleReq req);
    List<RoleResponse> getAllRoles();
}
