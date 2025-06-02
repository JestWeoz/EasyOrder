package com.example.easyoderba.Model.Entity.AuthEntity;


import com.example.easyoderba.Model.Entity.BaseEntity;
import com.example.easyoderba.Model.Entity.ProductEntity.InvoiceEntity;
import com.example.easyoderba.Model.Entity.ProductEntity.OrderEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity

public class User extends BaseEntity {
    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String username;
    String password;
    String firstName;
    String lastName;
    String email;
    String phone;

    @ManyToMany
    Set<Role> roles;

    @OneToMany(mappedBy = "user")
    private Collection<InvoiceEntity> invoices;

}
