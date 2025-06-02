package com.example.easyoderba.Model.DTO.response.ProductRes;

import com.example.easyoderba.Model.DTO.request.ProductReq.OrderItemReq;
import com.example.easyoderba.Utils.Enums.OrderStatus;
import com.example.easyoderba.Utils.Enums.PaymentMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRes {
    Long id;
    List<OrderItemRes> orderItems;
    TableRes table;
    BigDecimal totalAmount;
    String status;
    String note;
    Boolean isPaid;
    String created_at;
}
