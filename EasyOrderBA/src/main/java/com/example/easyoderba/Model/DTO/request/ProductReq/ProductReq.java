package com.example.easyoderba.Model.DTO.request.ProductReq;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReq {
    String name;
    String description;
    Long price;
    Long categoryId;
}
