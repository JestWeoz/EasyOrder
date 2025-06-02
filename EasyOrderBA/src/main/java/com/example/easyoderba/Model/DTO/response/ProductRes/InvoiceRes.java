package com.example.easyoderba.Model.DTO.response.ProductRes;

import com.example.easyoderba.Model.DTO.response.AuthRes.UserResponse;
import com.example.easyoderba.Utils.Enums.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceRes {
    List<OrderRes> orders;
    Long totalAmount;
    PaymentMethod paymentMethod;
    String customerName;
    String customerPhone;
    UserResponse user;

}
