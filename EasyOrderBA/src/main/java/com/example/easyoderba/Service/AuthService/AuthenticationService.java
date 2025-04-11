package com.example.easyoderba.Service.AuthService;

import com.example.easyoderba.Model.DTO.request.AuthReq.IntrospectReq;
import com.example.easyoderba.Model.DTO.request.AuthReq.LoginReq;
import com.example.easyoderba.Model.DTO.response.AuthRes.AuthenticationResponse;
import com.example.easyoderba.Model.DTO.response.AuthRes.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;


public interface AuthenticationService {
    AuthenticationResponse Login(LoginReq loginReq);

    IntrospectResponse Introspect(IntrospectReq introspectReq) throws JOSEException, ParseException;
}
