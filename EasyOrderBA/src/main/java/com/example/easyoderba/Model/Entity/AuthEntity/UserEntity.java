package com.example.easyoderba.Model.Entity.AuthEntity;


import com.example.easyoderba.Model.Entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tbl_user")
public class UserEntity extends BaseEntity {
    String username;
    String password;
    String firstName;
    String lastName;
    String email;
    String phone;

    @ManyToMany
    Set<RoleEntity> roles;

}
