package com.example.easyoderba.Service;


import com.example.easyoderba.Model.DTO.request.CreateUserReq;
import com.example.easyoderba.Model.DTO.request.UpdateUserReq;
import com.example.easyoderba.Model.DTO.response.UserResponse;

import java.util.List;


public interface UserService {
    String CreateUser(CreateUserReq createUserReq);
    List<UserResponse> getAllUsers();
    UserResponse GetUserById(Long id);
    String updateUser(UpdateUserReq updateUserReq);
}
