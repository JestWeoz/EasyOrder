package com.example.easyoderba.Model.DTO.response.ProductRes;


import com.example.easyoderba.Model.Entity.ProductEntity.ImageEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageResponse {
    Long id;
    String url;
}
