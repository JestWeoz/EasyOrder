package com.example.easyoderba.Model.DTO.request.ProductReq;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReq {
    Long id;
    String name;
    int status;
    String description;
    Long price;
    Long categoryId;
    List<MultipartFile> images;
}
