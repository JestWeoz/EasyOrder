package com.example.easyoderba.Model.DTO.request.ProductReq;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemReq {
    Long productId;
    int quantity;
}
