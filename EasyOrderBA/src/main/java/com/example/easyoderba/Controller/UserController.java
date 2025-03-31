package com.example.easyoderba.Controller;


import com.example.easyoderba.Model.DTO.request.AuthReq.CreateUserReq;
import com.example.easyoderba.Model.DTO.request.AuthReq.UpdateUserReq;
import com.example.easyoderba.Model.DTO.response.ApiResponse;
import com.example.easyoderba.Model.DTO.response.AuthRes.UserResponse;
import com.example.easyoderba.Service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody @Valid CreateUserReq createUserReq) {
        return ApiResponse.<String>builder()
                .result(userService.CreateUser(createUserReq))
                .build();
    }
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable Long id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.GetUserById(id))
                .build();
    }
    @GetMapping("/getAll")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }
    @PostMapping("/update")
    public ApiResponse<String> updateUser(@RequestBody  UpdateUserReq updateUserReq) {
        return ApiResponse.<String>builder()
                .result(userService.updateUser(updateUserReq))
                .build();
    }

}
