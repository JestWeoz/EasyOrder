package com.example.easyoderba.Controller;


import com.example.easyoderba.Model.DTO.request.CreateUserReq;
import com.example.easyoderba.Service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid CreateUserReq createUserReq) {
        userService.CreateUser(createUserReq);
        return ResponseEntity.ok(createUserReq);
    }
    @GetMapping("/{id}")
    @Tag(name = "Lấy user theo id")
    public ResponseEntity<?> getUser(@PathVariable Long id) {

        return ResponseEntity.ok().body(userService.GetUserById(id));
    }

}
