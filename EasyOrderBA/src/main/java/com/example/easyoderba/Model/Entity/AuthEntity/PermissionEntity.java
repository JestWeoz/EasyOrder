package com.example.easyoderba.Model.Entity.AuthEntity;


import com.example.easyoderba.Model.Entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tbl_permisstion")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class PermissionEntity extends BaseEntity {
    String permission;

}
