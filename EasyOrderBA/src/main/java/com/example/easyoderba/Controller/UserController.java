package com.example.easyoderba.Controller;


import com.example.easyoderba.Model.DTO.request.CreateUserReq;
import com.example.easyoderba.Model.DTO.response.ApiResponse;
import com.example.easyoderba.Model.DTO.response.UserResponse;
import com.example.easyoderba.Service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

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
    @Tag(name = "Lấy user theo id")
    public ApiResponse<UserResponse> getUser(@PathVariable Long id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.GetUserById(id))
                .build();
    }

}
