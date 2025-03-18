package com.example.easyoderba.Controller;


import com.example.easyoderba.Model.DTO.request.IntrospectReq;
import com.example.easyoderba.Model.DTO.request.LoginReq;
import com.example.easyoderba.Model.DTO.response.ApiResponse;
import com.example.easyoderba.Model.DTO.response.AuthenticationResponse;
import com.example.easyoderba.Model.DTO.response.IntrospectResponse;
import com.example.easyoderba.Service.AuthenticationService;
import com.example.easyoderba.Service.UserService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;


    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> login(@RequestBody LoginReq loginReq) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.Login(loginReq))
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectReq introspectReq) throws ParseException, JOSEException {
         var result = authenticationService.Introspect(introspectReq);
         return ApiResponse.<IntrospectResponse>builder()
                 .result(result)
                 .build();
    }
}
