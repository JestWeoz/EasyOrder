package com.example.easyoderba.Controller;


import com.example.easyoderba.Model.DTO.request.AuthReq.IntrospectReq;
import com.example.easyoderba.Model.DTO.request.AuthReq.LoginReq;
import com.example.easyoderba.Model.DTO.response.ApiResponse;
import com.example.easyoderba.Model.DTO.response.AuthenticationResponse;
import com.example.easyoderba.Model.DTO.response.IntrospectResponse;
import com.example.easyoderba.Service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;


    @GetMapping("/token")
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
