package com.example.easyoderba.Model.DTO.response.ProductRes;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {
    String qrCodeBase64;
    String checkoutUrl;
    Long orderCode;
    String status;
    String message;
} 