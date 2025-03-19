package com.example.easyoderba.Model.DTO.response;

import com.example.easyoderba.Model.Entity.AuthEntity.Role;
import com.example.easyoderba.Model.Entity.AuthEntity.User;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link User}
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

    Set<Role> roles;
}