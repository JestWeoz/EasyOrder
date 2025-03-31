package com.example.easyoderba.Model.DTO.request.AuthReq;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginReq {
    public String username;
    public String password;
}
