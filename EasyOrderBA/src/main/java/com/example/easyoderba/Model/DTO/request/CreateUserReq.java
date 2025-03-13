package com.example.easyoderba.Model.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserReq {
    @Size(min = 4, message = "Username must be at least 4 characters")
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String phone;
}
