package com.example.easyoderba.Model.DTO.request.AuthReq;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleReq {
    String name;
    String description;
    Set<Long> permissions;
}
