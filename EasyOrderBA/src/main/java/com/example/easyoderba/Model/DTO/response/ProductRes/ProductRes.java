package com.example.easyoderba.Model.DTO.response.ProductRes;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRes {
    String name;
    Long price;
    String description;
}
