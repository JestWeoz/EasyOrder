package com.example.easyoderba.Model.DTO.response;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.example.easyoderba.Model.Entity.AuthEntity.UserEntity}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse implements Serializable {
    String username;
    String firstName;
    String lastName;
    String email;
    String phone;
}