package org.easyoder.Controller;


import lombok.RequiredArgsConstructor;
import org.easyoder.Model.DTO.UserCreationReq;
import org.easyoder.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<?> Register (@RequestBody UserCreationReq userCreationReq) {
        userService.createUser(userCreationReq);
        return ResponseEntity.accepted().body("Succed");
    }
}
