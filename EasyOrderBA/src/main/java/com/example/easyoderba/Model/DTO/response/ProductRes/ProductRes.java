package com.example.easyoderba.Model.DTO.response.ProductRes;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRes {
    Long id;
    String name;
    int status;
    Long price;
    String description;
    List<ImageResponse> images;
}
