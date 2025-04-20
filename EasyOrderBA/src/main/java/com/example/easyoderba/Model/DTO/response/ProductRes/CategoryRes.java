package com.example.easyoderba.Model.DTO.response.ProductRes;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRes {
    Long id;
    List<ProductRes> products;
    String name;
}
