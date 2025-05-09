package com.example.easyoderba.Model.DTO.response.ProductRes;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableRes {
    Long id;
    String name;
    Long capacity;
    Long status;
    String description;
    String url;
}
