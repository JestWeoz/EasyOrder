package com.example.easyoderba.Model.Entity.AuthEntity;


import com.example.easyoderba.Model.Entity.BaseEntity;
import jakarta.persistence.*;
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
@Table(name = "tbl_role")
public class RoleEntity extends BaseEntity {
    String role;

    @ManyToMany
    Set<PermissionEntity> permissions;

}
