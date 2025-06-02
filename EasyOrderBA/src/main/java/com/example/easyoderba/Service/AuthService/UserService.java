package com.example.easyoderba.Service.AuthService;


import com.example.easyoderba.Model.DTO.request.AuthReq.CreateUserReq;
import com.example.easyoderba.Model.DTO.request.AuthReq.UpdateUserReq;
import com.example.easyoderba.Model.DTO.response.AuthRes.UserResponse;

import java.util.List;


public interface UserService {
    String CreateUser(CreateUserReq createUserReq);
    List<UserResponse> getAllUsers();
    UserResponse GetUserById(Long id);
    String updateUser(UpdateUserReq updateUserReq);
    UserResponse getUserByUserName(String userName);
}
