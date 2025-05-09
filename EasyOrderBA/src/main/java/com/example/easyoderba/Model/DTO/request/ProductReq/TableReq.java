package com.example.easyoderba.Model.DTO.request.ProductReq;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableReq {
    Long id;
    Long capacity;
    Long status;
    String description;
    String name;
}
