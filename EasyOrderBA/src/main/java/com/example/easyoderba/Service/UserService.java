package com.example.easyoderba.Service;


import com.example.easyoderba.Model.DTO.request.CreateUserReq;
import com.example.easyoderba.Model.DTO.request.LoginReq;
import com.example.easyoderba.Model.DTO.response.UserResponse;
import com.example.easyoderba.Model.Entity.AuthEntity.UserEntity;
import org.springframework.http.ResponseEntity;


public interface UserService {
    UserEntity CreateUser(CreateUserReq createUserReq);
    ResponseEntity<?> Login(LoginReq loginReq);
    UserResponse GetUserById(Long id);
}
